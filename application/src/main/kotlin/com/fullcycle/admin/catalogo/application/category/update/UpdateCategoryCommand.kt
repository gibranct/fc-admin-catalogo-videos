package com.fullcycle.admin.catalogo.application.category.update

data class UpdateCategoryCommand internal constructor(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
) {

    companion object {

        fun with(
            id: String,
            name: String,
            description: String,
            isActive: Boolean
        ): UpdateCategoryCommand {
            return UpdateCategoryCommand(id, name, description, isActive)
        }

    }

}