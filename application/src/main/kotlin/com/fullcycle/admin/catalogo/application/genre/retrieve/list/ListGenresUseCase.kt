package com.fullcycle.admin.catalogo.application.genre.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery

abstract class ListGenresUseCase: UseCase<SeachQuery, Pagination<ListGenresOutput>>() {
}