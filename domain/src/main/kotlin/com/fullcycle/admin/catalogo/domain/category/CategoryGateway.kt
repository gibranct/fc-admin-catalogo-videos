package com.fullcycle.admin.catalogo.domain.category

import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.domain.pagination.Pagination

interface CategoryGateway {

    fun create(aCategory: Category): Category

    fun deleteById(categoryId: String)

    fun findById(categoryId: String): Category?

    fun update(category: Category): Category

    fun findAll(aQuery: SearchQuery): Pagination<Category>

    fun existsByIds(categoryIds: Iterable<CategoryID>): List<CategoryID>

}