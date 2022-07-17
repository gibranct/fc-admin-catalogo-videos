package com.fullcycle.admin.catalogo.application.category.delete

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID

class DefaultDeleteCategoryUseCase(
    private val categoryGateway: CategoryGateway
): DeleteCategoryUseCase() {
    override fun execute(categoryId: String) {
        categoryGateway.deleteById(categoryId)
    }
}