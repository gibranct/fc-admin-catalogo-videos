package com.fullcycle.admin.catalogo.infrastructure.category.models

import java.time.Instant

data class CategoryResponse(
    val id: String,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
)