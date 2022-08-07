package com.fullcycle.admin.catalogo.application.genre.update

import com.fullcycle.admin.catalogo.domain.genre.Genre

data class UpdateGenreOutput(
    val id: String
) {

    companion object {

        fun from(
            genre: Genre
        ): UpdateGenreOutput {
            return UpdateGenreOutput(genre.id.value)
        }

    }

}