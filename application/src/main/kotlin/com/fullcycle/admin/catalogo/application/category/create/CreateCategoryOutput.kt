package com.fullcycle.admin.catalogo.application.category.create

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryID

data class CreateCategoryOutput private constructor(
    val id: String,
) {

    companion object {

        fun from(
            categoryID: String
        ): CreateCategoryOutput {
            return CreateCategoryOutput(categoryID)
        }

        fun from(
            category: Category
        ): CreateCategoryOutput {
            return CreateCategoryOutput(category.id.value)
        }

    }

}