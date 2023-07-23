package com.fullcycle.admin.catalogo.application.video.media.upload

import arrow.core.getOrElse
import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.video.*
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.*


class DefaultUploadMediaUseCase(
    private val mediaResourceGateway: MediaResourceGateway,
    private val videoGateway: VideoGateway,
) : UploadMediaUseCase() {

    override fun execute(anIn: UploadMediaCommand): UploadMediaOutput {
        val anId = VideoID.from(anIn.videoId)
        val aResource = anIn.videoResource

        val aVideo = videoGateway.findById(anId).toOption().getOrElse { throw notFound(anId) }

        val video = when (aResource.type) {
            VIDEO -> aVideo.updateVideoMedia(mediaResourceGateway.storeAudioVideo(anId, aResource))
            TRAILER -> aVideo.updateTrailerMedia(mediaResourceGateway.storeAudioVideo(anId, aResource))
            BANNER -> aVideo.updateBannerMedia(mediaResourceGateway.storeImage(anId, aResource))
            THUMBNAIL -> aVideo.updateThumbnailMedia(mediaResourceGateway.storeImage(anId, aResource))
            THUMBNAIL_HALF -> aVideo.updateThumbnailHalfMedia(mediaResourceGateway.storeImage(anId, aResource))
        }

        return UploadMediaOutput.with(videoGateway.update(video), aResource.type)
    }


    private fun notFound(anId: VideoID): NotFoundException {
        return NotFoundException.with(Video::class, anId)
    }
}