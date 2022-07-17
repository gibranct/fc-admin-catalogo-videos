package com.fullcycle.admin.catalogo.application.category.create

data class CreateCategoryCommand internal constructor(
    val name: String,
    val description: String?,
    val isActive: Boolean,
) {

    companion object {

        fun with(
            name: String,
            description: String?,
            isActive: Boolean
        ): CreateCategoryCommand {
            return CreateCategoryCommand(name, description, isActive)
        }

    }

}