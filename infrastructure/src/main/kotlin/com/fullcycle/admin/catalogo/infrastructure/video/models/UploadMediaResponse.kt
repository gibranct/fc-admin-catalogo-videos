package com.fullcycle.admin.catalogo.infrastructure.video.models

import com.fullcycle.admin.catalogo.domain.video.VideoMediaType

data class UploadMediaResponse(
    val videoId: String,
    val mediaType: VideoMediaType
)
