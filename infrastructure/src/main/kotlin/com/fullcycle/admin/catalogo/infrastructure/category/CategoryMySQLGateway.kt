package com.fullcycle.admin.catalogo.infrastructure.category

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.*
import javax.transaction.Transactional

@Component
class CategoryMySQLGateway(
    private val categoryRepository: CategoryRepository
): CategoryGateway {

    @Transactional
    override fun create(aCategory: Category): Category {
        return saveAndFlush(aCategory)
    }

    override fun deleteById(categoryId: String) {
        if (categoryRepository.existsById(categoryId))
            categoryRepository.deleteById(categoryId)
    }

    override fun findById(categoryId: String): Category? {
        val optionalCategory = categoryRepository.findById(categoryId)
        return when (optionalCategory.isPresent) {
            true -> optionalCategory.get().toAggregate()
            false -> null
        }
    }

    @Transactional
    override fun update(aCategory: Category): Category {
        return saveAndFlush(aCategory)
    }

    override fun findAll(aQuery: SeachQuery): Pagination<Category> {
        val page = PageRequest.of(
            aQuery.page,
            aQuery.perPage,
            Sort.by(Sort.Direction.fromString(aQuery.direction), aQuery.sort)
        )

        val specifications = Optional.ofNullable(aQuery.term)
            .filter { it.isNotBlank() }
            .map { term -> SpecificationUtils.like<CategoryJpaEntity>("name", term)
                        .or(SpecificationUtils.like("description", term))
            }
            .orElse(null)

        val pageResult = categoryRepository.findAll(Specification.where(specifications), page)

        return Pagination(
            currentPage = pageResult.number,
            perPage = pageResult.size,
            total = pageResult.totalElements,
            items = pageResult.content.map { it.toAggregate() }
        )
    }

    override fun existsById(categoryIds: Iterable<CategoryID>): List<CategoryID> {
        val ids = categoryIds.map { it.value }
        return categoryRepository.existsByIds(ids).map { CategoryID.from(it) }
    }

    private fun saveAndFlush(aCategory: Category): Category {
        return categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory))
            .toAggregate()
    }
}