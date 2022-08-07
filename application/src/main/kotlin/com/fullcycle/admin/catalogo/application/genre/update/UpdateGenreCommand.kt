package com.fullcycle.admin.catalogo.application.genre.update

data class UpdateGenreCommand(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val categoriesIds: List<String>,
) {

    companion object {

        fun with(
            id: String,
            name: String,
            isActive: Boolean,
            categoriesIds: List<String>,
        ): UpdateGenreCommand {
            return UpdateGenreCommand(id, name, isActive, categoriesIds)
        }

    }

}