package com.fullcycle.admin.catalogo.application.genre.retrieve.list

import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery

class DefaultListGenresUseCase(
    val genreGateway: GenreGateway
): ListGenresUseCase() {
    override fun execute(anIn: SeachQuery): Pagination<ListGenresOutput> {
        return genreGateway.findAll(anIn)
            .map(ListGenresOutput::from)
    }
}