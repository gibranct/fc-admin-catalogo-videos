package com.fullcycle.admin.catalogo.infrastructure.genre

import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.*

@Component
class GenreMySQLGateway(
    private val genreRepository: GenreRepository
): GenreGateway {
    override fun create(aGenre: Genre): Genre {
        return saveAndFlush(aGenre)
    }

    override fun deleteById(genreId: String) {
        if (genreRepository.existsById(genreId)) {
            genreRepository.deleteById(genreId)
        }
    }

    override fun findById(genreId: String): Genre? {
        val optionalGenre = genreRepository.findById(genreId)
        return when(optionalGenre.isPresent) {
            true -> optionalGenre.get().toAggregate()
            false -> null
        }
    }

    override fun update(genre: Genre): Genre {
        return saveAndFlush(genre)
    }

    override fun findAll(aQuery: SearchQuery): Pagination<Genre> {
        val page = PageRequest.of(
            aQuery.page,
            aQuery.perPage,
            Sort.by(Sort.Direction.fromString(aQuery.direction), aQuery.sort)
        )
        val specifications = Optional.ofNullable(aQuery.term)
            .filter { it.isNotBlank() }
            .map { term -> SpecificationUtils.like<GenreJpaEntity>("name", term) }
            .orElse(null)

        val pageResult = genreRepository.findAll(Specification.where(specifications), page)

        return Pagination(
            currentPage = pageResult.number,
            perPage = pageResult.size,
            total = pageResult.totalElements,
            items = pageResult.content.map { it.toAggregate() }
        )
    }

    override fun existsByIds(genreIds: Iterable<GenreID>): List<GenreID> {
        val ids = genreIds.map { it.value }
        return genreRepository.existsByIds(ids).map { GenreID.from(it) }
    }

    private fun saveAndFlush(aGenre: Genre): Genre {
        return genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre))
            .toAggregate()
    }
}