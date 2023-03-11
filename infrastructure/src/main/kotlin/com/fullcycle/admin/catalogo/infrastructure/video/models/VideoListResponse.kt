package com.fullcycle.admin.catalogo.infrastructure.video.models

import java.time.Instant


data class VideoListResponse(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedA: Instant,
)
