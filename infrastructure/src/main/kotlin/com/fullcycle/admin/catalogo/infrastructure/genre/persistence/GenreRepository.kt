package com.fullcycle.admin.catalogo.infrastructure.genre.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GenreRepository: JpaRepository<GenreJpaEntity, String> {

    fun findAll(where: Specification<GenreJpaEntity>, page: Pageable): Page<GenreJpaEntity>

}