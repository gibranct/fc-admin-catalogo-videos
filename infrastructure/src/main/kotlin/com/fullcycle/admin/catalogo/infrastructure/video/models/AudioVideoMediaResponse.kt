package com.fullcycle.admin.catalogo.infrastructure.video.models

data class AudioVideoMediaResponse(
    val id: String,
    val checksum: String,
    val name: String,
    val rawLocation: String,
    val encodedLocation: String,
    val status: String
)
