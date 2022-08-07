package com.fullcycle.admin.catalogo.domain.genre

import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery

interface GenreGateway {

    fun create(aGenre: Genre): Genre

    fun deleteById(genreId: String)

    fun findById(genreId: String): Genre?

    fun update(genre: Genre): Genre

    fun findAll(aQuery: SeachQuery): Pagination<Genre>

}