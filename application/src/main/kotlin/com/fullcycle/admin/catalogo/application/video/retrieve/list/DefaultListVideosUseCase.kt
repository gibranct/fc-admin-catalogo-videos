package com.fullcycle.admin.catalogo.application.video.retrieve.list

import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.video.VideoGateway
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery

class DefaultListVideosUseCase(
    private val videoGateway: VideoGateway,
) : ListVideosUseCase() {

    override fun execute(anIn: VideoSearchQuery): Pagination<VideoListOutput> {
        return videoGateway.findAll(anIn).map(VideoListOutput::from)
    }

}