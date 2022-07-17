package com.fullcycle.admin.catalogo.application.category.update

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryID

data class UpdateCategoryOutput internal constructor(
    val id: String,
) {

    companion object {

        fun from(
            category: Category
        ): UpdateCategoryOutput {
            return UpdateCategoryOutput(category.id.value)
        }


        fun from(
            id: String
        ): UpdateCategoryOutput {
            return UpdateCategoryOutput(id)
        }
    }

}