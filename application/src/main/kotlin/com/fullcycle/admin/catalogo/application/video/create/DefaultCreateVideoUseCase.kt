package com.fullcycle.admin.catalogo.application.video.create

import com.fullcycle.admin.catalogo.domain.Identifier
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway
import com.fullcycle.admin.catalogo.domain.video.Rating
import com.fullcycle.admin.catalogo.domain.video.Video
import com.fullcycle.admin.catalogo.domain.video.VideoGateway
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.BANNER
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL_HALF
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.VIDEO
import com.fullcycle.admin.catalogo.domain.video.VideoResource


class DefaultCreateVideoUseCase(
    private val categoryGateway: CategoryGateway,
    private val castMemberGateway: CastMemberGateway,
    private val genreGateway: GenreGateway,
    private val mediaResourceGateway: MediaResourceGateway,
    private val videoGateway: VideoGateway,
) : CreateVideoUseCase() {

    override fun execute(anIn: CreateVideoCommand): CreateVideoOutput {
        val aRating = Rating.valueOf(anIn.rating)
        val aLaunchYear = anIn.launchedAt
        val categories = toIdentifier(anIn.categories, CategoryID::from)
        val genres = toIdentifier(anIn.genres, GenreID::from)
        val members = toIdentifier(anIn.members, CastMemberID::from)

        val notification = Notification.create()
        notification.append(validateCategories(categories))
        notification.append(validateGenres(genres))
        notification.append(validateMembers(members))

        val aVideo = Video.newVideo(
            anIn.title,
            anIn.description,
            aLaunchYear,
            anIn.duration,
            anIn.opened,
            anIn.published,
            aRating,
            categories,
            genres,
            members
        )

        aVideo.validate(notification)

        if (notification.hasError()) {
            throw NotificationException("Could not create Aggregate Video", notification)
        }

        return CreateVideoOutput.from(create(anIn, aVideo))
    }

    private fun validateCategories(ids: Set<CategoryID>): ValidationHandler {
        return validateAggregate("categories", ids, categoryGateway::existsByIds)
    }

    private fun validateGenres(ids: Set<GenreID>): ValidationHandler {
        return validateAggregate("genres", ids, genreGateway::existsByIds)
    }

    private fun validateMembers(ids: Set<CastMemberID>): ValidationHandler {
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds)
    }

    private fun create(aCommand: CreateVideoCommand, aVideo: Video): Video {
        val anId = aVideo.id
        return try {
            val aVideoMedia = aCommand.video?.let { this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(VIDEO, it)) }

            val aTrailerMedia = aCommand.trailer?.let { this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(TRAILER, it)) }

            val aBannerMedia = aCommand.banner?.let { this.mediaResourceGateway.storeImage(anId, VideoResource.with(BANNER, it)) }

            val aThumbnailMedia = aCommand.thumbnail?.let { this.mediaResourceGateway.storeImage(anId, VideoResource.with(THUMBNAIL, it)) }

            val aThumbHalfMedia = aCommand.thumbnailHalf?.let { this.mediaResourceGateway.storeImage(anId, VideoResource.with(THUMBNAIL_HALF, it)) }

            videoGateway.create(
                aVideo
                    .updateVideoMedia(aVideoMedia)
                    .updateTrailerMedia(aTrailerMedia)
                    .updateBannerMedia(aBannerMedia)
                    .updateThumbnailMedia(aThumbnailMedia)
                    .updateThumbnailHalfMedia(aThumbHalfMedia)
            )
        } catch (t: Throwable) {
            this.mediaResourceGateway.clearResources(anId)
            throw InternalErrorException.with("An error on create video was observed [videoId:${anId.value}]", t)
        }
    }

    private fun <T : Identifier> validateAggregate(
        aggregate: String,
        ids: Set<T>,
        existsByIds: (iterable: Iterable<T>) -> List<T>
    ): ValidationHandler {
        val notification = Notification.create()
        if (ids.isEmpty()) {
            return notification
        }
        val retrievedIds = existsByIds(ids)
        if (ids.size != retrievedIds.size) {
            val missingIds = ArrayList(ids)
            missingIds.removeAll(retrievedIds.toSet())
            val missingIdsMessage = missingIds
                .map(Identifier::value)
                .joinToString(",")

            notification.append(Error("Some $aggregate could not be found: $missingIdsMessage"))
        }
        return notification
    }

    private fun <T> toIdentifier(ids: Set<String>, mapper: (id: String) -> T): Set<T> {
        return ids.map(mapper).toSet()
    }
}