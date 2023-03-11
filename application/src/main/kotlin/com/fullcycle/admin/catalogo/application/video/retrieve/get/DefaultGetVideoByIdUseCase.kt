package com.fullcycle.admin.catalogo.application.video.retrieve.get

import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.video.Video
import com.fullcycle.admin.catalogo.domain.video.VideoGateway
import com.fullcycle.admin.catalogo.domain.video.VideoID

class DefaultGetVideoByIdUseCase(
    private val videoGateway: VideoGateway,
) : GetVideoByIdUseCase() {


    override fun execute(anIn: String): VideoOutput {
        val videoId = VideoID.from(anIn)
        return videoGateway.findById(videoId)
            .toOption()
            .fold({ throw NotFoundException.with(Video::class, videoId) }, VideoOutput::from)
    }
}