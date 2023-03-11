package com.fullcycle.admin.catalogo.infrastructure.video.models

data class UpdateVideoRequest(
    val title: String,
    val description: String,
    val duration: Double,
    val yearLaunched: Int,
    val opened: Boolean,
    val published: Boolean,
    val rating: String,
    val castMembers: Set<String>,
    val categories: Set<String>,
    val genres: Set<String>
)
