package com.fullcycle.admin.catalogo.infrastructure.category.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<CategoryJpaEntity, String> {
    fun findAll(where: Specification<CategoryJpaEntity>, page: Pageable): Page<CategoryJpaEntity>

    @Query("select id from Category where id in :ids")
    fun existsByIds(@Param("ids") ids: List<String>): List<String>
}