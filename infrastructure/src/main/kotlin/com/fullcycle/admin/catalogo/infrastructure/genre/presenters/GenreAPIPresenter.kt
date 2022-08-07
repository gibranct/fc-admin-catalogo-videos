package com.fullcycle.admin.catalogo.infrastructure.genre.presenters

import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenresOutput
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse

class GenreAPIPresenter {

    companion object {

        fun present(genreOutput: GenreOutput) = GenreResponse(
            id = genreOutput.id,
            name = genreOutput.name,
            active = genreOutput.active,
            createdAt = genreOutput.createdAt,
            updatedAt = genreOutput.updatedAt,
            deletedAt = genreOutput.deletedAt,
            categories = genreOutput.categoriesIds.map { it.value }
        )

        fun present(listGenresOutput: ListGenresOutput) = GenreListResponse (
            id = listGenresOutput.id,
            name = listGenresOutput.name,
            active = listGenresOutput.active,
            createdAt = listGenresOutput.createdAt
        )

    }

}