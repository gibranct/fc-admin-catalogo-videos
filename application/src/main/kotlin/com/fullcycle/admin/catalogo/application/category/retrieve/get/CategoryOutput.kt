package com.fullcycle.admin.catalogo.application.category.retrieve.get

import com.fullcycle.admin.catalogo.domain.category.Category
import java.time.Instant

data class CategoryOutput internal constructor(
    val id: String,
    var name: String,
    var description: String?,
    var isActive: Boolean,
    val createdAt: Instant,
    var updatedAt: Instant,
    var deletedAt: Instant?,
) {

    companion object {

        fun from(category: Category): CategoryOutput {
            return CategoryOutput(
                category.id.value,
                category.name,
                category.description,
                category.isActive,
                category.createdAt,
                category.updatedAt,
                category.deletedAt
            )
        }

    }

}