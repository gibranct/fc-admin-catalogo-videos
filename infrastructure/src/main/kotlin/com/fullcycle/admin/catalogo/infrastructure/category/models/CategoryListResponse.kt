package com.fullcycle.admin.catalogo.infrastructure.category.models

import java.time.Instant

data class CategoryListResponse(
    val id: String,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Instant,
    val deletedAt: Instant?,
) {
}