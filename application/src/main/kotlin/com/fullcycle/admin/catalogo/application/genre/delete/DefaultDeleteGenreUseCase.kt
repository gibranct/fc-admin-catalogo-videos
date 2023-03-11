package com.fullcycle.admin.catalogo.application.genre.delete

import com.fullcycle.admin.catalogo.domain.genre.GenreGateway

data class DefaultDeleteGenreUseCase(
    private val genreGateway: GenreGateway
): DeleteGenreUseCase() {
    override fun execute(anIn: String) {
        genreGateway.deleteById(anIn)
    }
}