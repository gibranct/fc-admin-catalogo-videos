package com.fullcycle.admin.catalogo.application.genre.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery

abstract class ListGenresUseCase: UseCase<SearchQuery, Pagination<ListGenresOutput>>() {
}