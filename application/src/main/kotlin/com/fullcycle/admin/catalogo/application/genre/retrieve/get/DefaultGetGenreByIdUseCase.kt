package com.fullcycle.admin.catalogo.application.genre.retrieve.get

import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID

class DefaultGetGenreByIdUseCase(
    val genreGateway: GenreGateway
): GetGenreByIdUseCase() {
    override fun execute(anIn: String): GenreOutput {
        val genreId = GenreID.from(anIn)
        return genreGateway
            .findById(anIn)
            .toOption()
            .fold(notFound(genreId), GenreOutput::from)
    }

    private fun notFound(genreId: GenreID): () -> Nothing {
        return {
            throw NotFoundException.with(Genre::class, genreId)
        }
    }
}