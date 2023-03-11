package com.fullcycle.admin.catalogo.infrastructure.video.models

import java.time.Instant

data class VideoResponse(
    val id: String,
    val title: String,
    val description: String,
    val yearLaunched: Int,
    val duration: Double,
    val opened: Boolean,
    val published: Boolean,
    val rating: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val banner: ImageMediaResponse,
    val thumbnail: ImageMediaResponse,
    val thumbnailHalf: ImageMediaResponse,
    val video: AudioVideoMediaResponse,
    val trailer: AudioVideoMediaResponse,
    val categoriesId: Set<String>,
    val genresId: Set<String>,
    val castMembersI: Set<String>,
)
