package com.fullcycle.admin.catalogo.application.video.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery

abstract class ListVideosUseCase : UseCase<VideoSearchQuery, Pagination<VideoListOutput>>()