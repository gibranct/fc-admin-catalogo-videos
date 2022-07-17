package com.fullcycle.admin.catalogo.infrastructure.category.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<CategoryJpaEntity, String> {
    fun findAll(where: Specification<CategoryJpaEntity>, page: Pageable): Page<CategoryJpaEntity>
}