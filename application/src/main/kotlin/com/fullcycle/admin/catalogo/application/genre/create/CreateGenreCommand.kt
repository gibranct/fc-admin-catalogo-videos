package com.fullcycle.admin.catalogo.application.genre.create

data class CreateGenreCommand(
    val name: String,
    val isActive: Boolean = true,
    val categoriesIds: List<String>
) {


    companion object {

        fun with(
            name: String,
            isActive: Boolean,
            categoriesIds: List<String>
        ): CreateGenreCommand {
            return CreateGenreCommand(name, isActive, categoriesIds)
        }

    }

}