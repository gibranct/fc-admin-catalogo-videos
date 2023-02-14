package com.fullcycle.admin.catalogo.application.category.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.domain.pagination.Pagination

abstract class ListCategoriesUseCase: UseCase<SearchQuery, Pagination<ListCategoriesOutput>>() {
}