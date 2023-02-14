package com.fullcycle.admin.catalogo.infrastructure.api.controllers

import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreCommand
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenresUseCase
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreCommand
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.infrastructure.api.GenreAPI
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.fullcycle.admin.catalogo.infrastructure.genre.presenters.GenreAPIPresenter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class GenreController(
    private val creteGenreUseCase: CreateGenreUseCase,
    private val getGenreByIdUseCase: GetGenreByIdUseCase,
    private val updateGenreUseCase: UpdateGenreUseCase,
    private val deleteGenreUseCase: DeleteGenreUseCase,
    private val listGenresUseCase: ListGenresUseCase,
): GenreAPI {
    override fun create(input: CreateGenreRequest): ResponseEntity<*> {
        val command = CreateGenreCommand.with(
            name = input.name,
            isActive = input.active,
            categoriesIds = input.categories.orEmpty()
        )
        val createGenreOutput = creteGenreUseCase.execute(command)
        return ResponseEntity
            .created(URI.create("/genres/${createGenreOutput.id}"))
            .body(createGenreOutput)
    }

    override fun updateById(id: String, input: UpdateGenreRequest): ResponseEntity<*> {
        val command = UpdateGenreCommand.with(
            id = id,
            name = input.name,
            isActive = input.active,
            categoriesIds = input.categories.orEmpty()
        )
        val updateGenreOutput = updateGenreUseCase.execute(command)
        return ResponseEntity.ok(updateGenreOutput)
    }

    override fun list(
        search: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: String
    ): Pagination<GenreListResponse> {
        val outputPagination = listGenresUseCase.execute(
            SearchQuery(
                page = page,
                perPage = perPage,
                term = search,
                sort = sort,
                direction = direction
            )
        )
        return Pagination(
            currentPage = outputPagination.currentPage,
            perPage = outputPagination.perPage,
            total = outputPagination.total,
            items = outputPagination.items.map(GenreAPIPresenter::present)
        )
    }

    override fun findById(id: String): GenreResponse {
        val genreOutput = getGenreByIdUseCase.execute(id)
        return GenreAPIPresenter.present(genreOutput)
    }

    override fun deleteById(id: String) {
        deleteGenreUseCase.execute(id)
    }
}