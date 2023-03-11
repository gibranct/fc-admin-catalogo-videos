package com.fullcycle.admin.catalogo.application.video.update

import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.Identifier
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.fullcycle.admin.catalogo.domain.video.MediaSourceGateway
import com.fullcycle.admin.catalogo.domain.video.Rating
import com.fullcycle.admin.catalogo.domain.video.Video
import com.fullcycle.admin.catalogo.domain.video.VideoGateway
import com.fullcycle.admin.catalogo.domain.video.VideoID

class DefaultUpdateVideoUseCase(
    private val categoryGateway: CategoryGateway,
    private val castMemberGateway: CastMemberGateway,
    private val genreGateway: GenreGateway,
    private val mediaResourceGateway: MediaSourceGateway,
    private val videoGateway: VideoGateway,
) : UpdateVideoUseCase() {

    override fun execute(anIn: UpdateVideoCommand): UpdateVideoOutput {
        val anId = VideoID.from(anIn.id)
        val aRating = Rating.valueOf(anIn.rating)
        val aLaunchYear = anIn.launchedAt
        val categories = toIdentifier(anIn.categories, CategoryID::from)
        val genres = toIdentifier(anIn.genres, GenreID::from)
        val members = toIdentifier(anIn.members, CastMemberID::from)

        val video = videoGateway.findById(anId)
            .toOption()
            .fold({ throw NotFoundException.with(Video::class, anId) }, { it })


        val notification = Notification.create()
        notification.append(validateCategories(categories))
        notification.append(validateGenres(genres))
        notification.append(validateMembers(members))

        val aVideo = video.update(
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

        return UpdateVideoOutput.from(update(anIn, video))
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

    private fun update(aCommand: UpdateVideoCommand, aVideo: Video): Video {
        val anId = aVideo.id

        return try {
            val aVideoMedia = aCommand.video?.let { this.mediaResourceGateway.storeAudioVideo(anId, it) }

            val aTrailerMedia = aCommand.trailer?.let { this.mediaResourceGateway.storeAudioVideo(anId, it) }

            val aBannerMedia = aCommand.banner?.let { this.mediaResourceGateway.storeImage(anId, it) }

            val aThumbnailMedia = aCommand.thumbnail?.let { this.mediaResourceGateway.storeImage(anId, it) }

            val aThumbHalfMedia = aCommand.thumbnailHalf?.let { this.mediaResourceGateway.storeImage(anId, it) }

            videoGateway.update(
                aVideo
                    .updateVideoMedia(aVideoMedia)
                    .updateTrailerMedia(aTrailerMedia)
                    .updateBannerMedia(aBannerMedia)
                    .updateThumbnailMedia(aThumbnailMedia)
                    .updateThumbnailHalfMedia(aThumbHalfMedia)
            )
        } catch (t: Throwable) {
            this.mediaResourceGateway.clearResources(anId)
            throw InternalErrorException.with("An error on update video was observed [videoId:${anId.value}]", t)
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