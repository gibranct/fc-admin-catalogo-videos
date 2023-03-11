package com.fullcycle.admin.catalogo.infrastructure.video.models

data class ImageMediaResponse(
    val id: String,
    val checksum: String,
    val name: String,
    val location: String
)
