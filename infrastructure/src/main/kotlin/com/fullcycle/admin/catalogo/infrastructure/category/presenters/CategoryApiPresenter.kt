package com.fullcycle.admin.catalogo.infrastructure.category.presenters

import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutput
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesOutput
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryListResponse

class CategoryApiPresenter {

    companion object {

        fun present(categoryOutput: CategoryOutput) = CategoryResponse(
            id = categoryOutput.id,
            name = categoryOutput.name,
            description = categoryOutput.description,
            isActive = categoryOutput.isActive,
            createdAt = categoryOutput.createdAt,
            updatedAt = categoryOutput.updatedAt,
            deletedAt = categoryOutput.deletedAt,
        )

        fun present(listCategoriesOutput: ListCategoriesOutput) = CategoryListResponse (
            id = listCategoriesOutput.id,
            name = listCategoriesOutput.name,
            description = listCategoriesOutput.description,
            isActive = listCategoriesOutput.isActive,
            createdAt = listCategoriesOutput.createdAt,
            deletedAt = listCategoriesOutput.deletedAt,
        )

    }

}