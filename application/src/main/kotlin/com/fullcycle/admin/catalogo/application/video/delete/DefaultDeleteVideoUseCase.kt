package com.fullcycle.admin.catalogo.application.video.delete

import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway
import com.fullcycle.admin.catalogo.domain.video.VideoGateway
import com.fullcycle.admin.catalogo.domain.video.VideoID

class DefaultDeleteVideoUseCase(
    private val videoGateway: VideoGateway,
    private val mediaResourceGateway: MediaResourceGateway,
) : DeleteVideoUseCase() {


    override fun execute(anIn: String) {
        val videoId = VideoID.from(anIn)
        videoGateway.deleteById(videoId)
        mediaResourceGateway.clearResources(videoId)
    }
}