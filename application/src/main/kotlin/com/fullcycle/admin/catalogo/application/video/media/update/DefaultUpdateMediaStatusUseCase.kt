package com.fullcycle.admin.catalogo.application.video.media.update

import arrow.core.getOrElse
import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.video.*
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType.VIDEO


class DefaultUpdateMediaStatusUseCase(
    private val videoGateway: VideoGateway,
) : UpdateMediaStatusUseCase() {

    override fun execute(anIn: UpdateMediaStatusCommand) {
        val anId = VideoID.from(anIn.videoId)
        val aResourceId = anIn.resourceId
        val folder = anIn.folder
        val filename = anIn.filename

        val aVideo = videoGateway.findById(anId)
            .toOption()
            .getOrElse { throw notFound(anId) }

        val encodedPath = "$folder/$filename"

        if (matches(aResourceId, aVideo.video)) {
            updateVideo(VIDEO, anIn.status, aVideo, encodedPath)
        } else if (matches(aResourceId, aVideo.trailer.toOption().orNull())) {
            updateVideo(TRAILER, anIn.status, aVideo, encodedPath)
        }
    }

    private fun updateVideo(aType: VideoMediaType, aStatus: MediaStatus, aVideo: Video, encodedPath: String) {
        when (aStatus) {
            MediaStatus.PENDING -> {}
            MediaStatus.PROCESSING -> aVideo.processing(aType)
            MediaStatus.COMPLETED -> aVideo.completed(aType, encodedPath)
        }
        videoGateway.update(aVideo)
    }

    private fun matches(anId: String, aMedia: AudioVideoMedia?): Boolean {
        return (aMedia?.id == anId)
    }

    private fun notFound(anId: VideoID): NotFoundException {
        return NotFoundException.with(Video::class, anId)
    }

}