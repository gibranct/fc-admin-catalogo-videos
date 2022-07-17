package com.fullcycle.admin.catalogo.application.category.retrieve.list

import com.fullcycle.admin.catalogo.domain.category.Category
import java.time.Instant

data class ListCategoriesOutput internal constructor(
    val id: String,
    var name: String,
    var description: String?,
    var isActive: Boolean,
    val createdAt: Instant,
    var deletedAt: Instant?,
) {

    companion object {

        fun from(aCategory: Category): ListCategoriesOutput {
            return ListCategoriesOutput(
                aCategory.id.value,
                aCategory.name,
                aCategory.description,
                aCategory.isActive,
                aCategory.createdAt,
                aCategory.deletedAt
            )
        }

    }

}