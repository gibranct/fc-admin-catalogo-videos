package com.fullcycle.admin.catalogo.infrastructure.genre.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GenreRepository: JpaRepository<GenreJpaEntity, String> {

    fun findAll(where: Specification<GenreJpaEntity>, page: Pageable): Page<GenreJpaEntity>


    @Query("select id from Genre where id in :ids")
    fun existsByIds(@Param("ids") ids: List<String>): List<String>
}