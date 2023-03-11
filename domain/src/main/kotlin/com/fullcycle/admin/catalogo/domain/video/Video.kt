package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.AggregateRoot
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import java.time.Instant


data class Video constructor(
    val videoID: VideoID,
    var title: String,
    var description: String,
    var launchedAt: Int,
    var duration: Double,
    var rating: Rating,
    var opened: Boolean,
    var published: Boolean,
    val createdAt: Instant,
    var updatedAt: Instant,
    var banner: ImageMedia? = null,
    var thumbnail: ImageMedia? = null,
    var thumbnailHalf: ImageMedia? = null,
    var video: AudioVideoMedia? = null,
    var trailer: AudioVideoMedia? = null,
    var genres: Set<GenreID>,
    var categories: Set<CategoryID>,
    var castMembers: Set<CastMemberID>,
) : AggregateRoot<VideoID>(videoID) {


    override fun validate(handler: ValidationHandler) {
        VideoValidator(this, handler).validate()
    }

    companion object {

        fun newVideo(
            title: String,
            description: String,
            launchedAt: Int,
            duration: Double,
            opened: Boolean,
            published: Boolean,
            rating: Rating,
            categories: Set<CategoryID>,
            genres: Set<GenreID>,
            castMember: Set<CastMemberID>,
        ): Video {
            val now = InstantUtils.now()
            val anId = VideoID.unique()
            return Video(
                videoID = anId,
                title = title,
                description = description,
                launchedAt = launchedAt,
                duration = duration,
                rating = rating,
                opened = opened,
                published = published,
                createdAt = now,
                updatedAt = now,
                banner = null,
                thumbnail = null,
                thumbnailHalf = null,
                video = null,
                trailer = null,
                genres = genres,
                categories = categories,
                castMembers = castMember,
            )
        }

        fun with(
            aVideo: Video,
        ): Video {
            return Video(
                videoID = aVideo.videoID,
                title = aVideo.title,
                description = aVideo.description,
                launchedAt = aVideo.launchedAt,
                duration = aVideo.duration,
                rating = aVideo.rating,
                opened = aVideo.opened,
                published = aVideo.published,
                createdAt = aVideo.createdAt,
                updatedAt = aVideo.updatedAt,
                banner = aVideo.banner,
                thumbnail = aVideo.thumbnail,
                thumbnailHalf = aVideo.thumbnailHalf,
                video = aVideo.video,
                trailer = aVideo.trailer,
                genres = aVideo.genres.toSet(),
                categories = aVideo.categories.toSet(),
                castMembers = aVideo.castMembers.toSet(),
            )
        }

        fun with(
            anId: VideoID,
            aTitle: String,
            aDescription: String,
            aLaunchYear: Int,
            aDuration: Double,
            wasOpened: Boolean,
            wasPublished: Boolean,
            aRating: Rating,
            aCreationDate: Instant,
            aUpdateDate: Instant,
            aBanner: ImageMedia?,
            aThumb: ImageMedia?,
            aThumbHalf: ImageMedia?,
            aTrailer: AudioVideoMedia?,
            aVideo: AudioVideoMedia?,
            categories: Set<CategoryID>,
            genres: Set<GenreID>,
            members: Set<CastMemberID>
        ) = Video(
            videoID = anId,
            title = aTitle,
            description = aDescription,
            launchedAt = aLaunchYear,
            duration = aDuration,
            rating = aRating,
            opened = wasOpened,
            published = wasPublished,
            createdAt = aCreationDate,
            updatedAt = aUpdateDate,
            banner = aBanner,
            thumbnail = aThumb,
            thumbnailHalf = aThumbHalf,
            video = aVideo,
            trailer = aTrailer,
            genres = genres.toSet(),
            categories = categories.toSet(),
            castMembers = members.toSet(),
        )

    }

    fun update(
        aTitle: String,
        aDescription: String,
        aLaunchYear: Int,
        aDuration: Double,
        wasOpened: Boolean,
        wasPublished: Boolean,
        aRating: Rating,
        categories: Set<CategoryID>,
        genres: Set<GenreID>,
        members: Set<CastMemberID>
    ): Video {
        title = aTitle
        description = aDescription
        launchedAt = aLaunchYear
        duration = aDuration
        opened = wasOpened
        published = wasPublished
        rating = aRating
        this.categories = categories.toSet()
        this.genres = genres.toSet()
        castMembers = members.toSet()
        updatedAt = InstantUtils.now()
        return this
    }

    fun updateBannerMedia(banner: ImageMedia?): Video {
        this.banner = banner
        updatedAt = InstantUtils.now()
        return this
    }

    fun updateThumbnailMedia(thumbnail: ImageMedia?): Video {
        this.thumbnail = thumbnail
        updatedAt = InstantUtils.now()
        return this
    }

    fun updateThumbnailHalfMedia(thumbnailHalf: ImageMedia?): Video {
        this.thumbnailHalf = thumbnailHalf
        updatedAt = InstantUtils.now()
        return this
    }

    fun updateTrailerMedia(trailer: AudioVideoMedia?): Video {
        this.trailer = trailer
        updatedAt = InstantUtils.now()
        onAudioVideoMediaUpdated(trailer)
        return this
    }

    fun updateVideoMedia(video: AudioVideoMedia?): Video {
        this.video = video
        updatedAt = InstantUtils.now()
        onAudioVideoMediaUpdated(video)
        return this
    }

    fun processing(aType: VideoMediaType): Video {
        if (VideoMediaType.VIDEO === aType) {
            video?.let { media -> updateVideoMedia(media) }
        } else if (VideoMediaType.TRAILER === aType) {
            trailer?.let { media -> updateTrailerMedia(media.processing()) }
        }
        return this
    }

    fun completed(aType: VideoMediaType, encodedPath: String): Video {
        if (VideoMediaType.VIDEO === aType) {
            video?.let { media -> updateVideoMedia(media.completed(encodedPath)) }
        } else if (VideoMediaType.TRAILER === aType) {
            trailer?.let {  media -> updateTrailerMedia(media.completed(encodedPath)) }
        }
        return this
    }

    private fun onAudioVideoMediaUpdated(media: AudioVideoMedia?) {
        if (media != null && media.isPendingEncode()) {
//            this.registerEvent(VideoMediaCreated.with(id.value, media.rawLocation))
        }
    }
}