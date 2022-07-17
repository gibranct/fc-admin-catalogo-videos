package com.fullcycle.admin.catalogo.infrastructure.api.controllers

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryCommand
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase
import com.fullcycle.admin.catalogo.domain.category.CategorySeachQuery
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import com.fullcycle.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CategoryController(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val listCategoriesUseCase: ListCategoriesUseCase,
): CategoryAPI {

    override fun create(input: CreateCategoryRequest): ResponseEntity<*> {
        val command = CreateCategoryCommand.with(
            name = input.name,
            description = input.description,
            isActive = input.isActive
        )

        return createCategoryUseCase.execute(command)
            .fold(
                { ResponseEntity.unprocessableEntity().body(it) },
                { ResponseEntity.created(URI.create("/categories/${it.id}")).body(it) }
            )
    }

    override fun updateById(id: String, input: UpdateCategoryRequest): ResponseEntity<*> {
        val command = UpdateCategoryCommand.with(
            id = id,
            name = input.name,
            description = input.description,
            isActive = input.isActive
        )

        return updateCategoryUseCase.execute(command)
            .fold(
                { ResponseEntity.unprocessableEntity().body(it) },
                { ResponseEntity.ok(it) }
            )
    }


    override fun list(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String
    ): Pagination<*> {
        val pagination = listCategoriesUseCase.execute(
            CategorySeachQuery(
                page = page,
                perPage = perPage,
                term = search,
                sort = sort,
                direction = direction
            )
        ).map { CategoryApiPresenter.present(it) }

        return pagination
    }

    override fun findById(id: String): CategoryResponse {
        return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id))
    }

    override fun deleteById(id: String) {
        deleteCategoryUseCase.execute(id)
    }
}