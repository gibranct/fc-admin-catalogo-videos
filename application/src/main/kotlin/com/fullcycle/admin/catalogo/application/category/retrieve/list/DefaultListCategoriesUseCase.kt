package com.fullcycle.admin.catalogo.application.category.retrieve.list

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery
import com.fullcycle.admin.catalogo.domain.pagination.Pagination

class DefaultListCategoriesUseCase(
    private val categoryGateway: CategoryGateway
): ListCategoriesUseCase() {
    override fun execute(anIn: SeachQuery): Pagination<ListCategoriesOutput> {
        return categoryGateway.findAll(anIn)
            .map(ListCategoriesOutput::from)
    }
}