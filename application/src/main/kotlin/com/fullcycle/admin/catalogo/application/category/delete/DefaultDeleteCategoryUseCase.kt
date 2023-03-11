package com.fullcycle.admin.catalogo.application.category.delete

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway

class DefaultDeleteCategoryUseCase(
    private val categoryGateway: CategoryGateway
): DeleteCategoryUseCase() {
    override fun execute(anIn: String) {
        categoryGateway.deleteById(anIn)
    }
}