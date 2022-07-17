package com.fullcycle.admin.catalogo.application.category.retrieve.get

import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException

class DefaultGetCategoryByIdUseCase(
    private val categoryGateway: CategoryGateway
): GetCategoryByIdUseCase() {
    override fun execute(anIn: String): CategoryOutput {
        val categoryID = CategoryID.from(anIn)
        return categoryGateway.findById(anIn)
            .toOption()
            .fold(notFound(categoryID), CategoryOutput::from)
    }

    private fun notFound(categoryID: CategoryID): () -> Nothing {
        return {
            throw NotFoundException.with(Category::class, categoryID)
        }
    }
}
