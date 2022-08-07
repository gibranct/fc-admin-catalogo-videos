package com.fullcycle.admin.catalogo.domain.category

import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery
import com.fullcycle.admin.catalogo.domain.pagination.Pagination

interface CategoryGateway {

    fun create(aCategory: Category): Category

    fun deleteById(categoryId: String)

    fun findById(categoryId: String): Category?

    fun update(category: Category): Category

    fun findAll(aQuery: SeachQuery): Pagination<Category>
    fun existsById(categoryIds: Iterable<CategoryID>): List<CategoryID>

}