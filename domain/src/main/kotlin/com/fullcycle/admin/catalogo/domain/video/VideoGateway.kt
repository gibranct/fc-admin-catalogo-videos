package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.pagination.Pagination


interface VideoGateway {

    fun create(aVideo: Video): Video

    fun deleteById(anId: VideoID)

    fun findById(anId: VideoID): Video?

    fun update(aVideo: Video): Video

    fun findAll(aQuery: VideoSearchQuery): Pagination<VideoPreview>

}