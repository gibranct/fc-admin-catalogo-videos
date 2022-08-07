package com.fullcycle.admin.catalogo.application.genre.create

import com.fullcycle.admin.catalogo.domain.genre.Genre

data class CreateGenreOutput internal constructor(
    val id: String,
) {

    companion object {

        fun from(
            genreID: String
        ): CreateGenreOutput {
            return CreateGenreOutput(genreID)
        }

        fun from(
            genre: Genre
        ): CreateGenreOutput {
            return CreateGenreOutput(genre.id.value)
        }

    }

}