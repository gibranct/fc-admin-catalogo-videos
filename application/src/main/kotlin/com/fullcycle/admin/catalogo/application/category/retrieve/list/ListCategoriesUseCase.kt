package com.fullcycle.admin.catalogo.application.category.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.category.CategorySeachQuery
import com.fullcycle.admin.catalogo.domain.pagination.Pagination

abstract class ListCategoriesUseCase: UseCase<CategorySeachQuery, Pagination<ListCategoriesOutput>>() {
}