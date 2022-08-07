package com.fullcycle.admin.catalogo.infrastructure.api

import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping(
    "/genres"
)
@Tag(name = "Genres")
interface GenreAPI {

    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun create(@RequestBody input: CreateGenreRequest): ResponseEntity<*>

    @PutMapping("/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Update a genre")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Updated successfully"),
        ApiResponse(responseCode = "404", description = "Genre not found"),
        ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun updateById(@PathVariable id: String, @RequestBody input: UpdateGenreRequest): ResponseEntity<*>

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "List genres paginated")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List genres successfully"),
        ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun list(
        @RequestParam() search: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") perPage: Int,
        @RequestParam(defaultValue = "name") sort: String,
        @RequestParam(name = "dir", defaultValue = "asc") direction: String,
    ): Pagination<GenreListResponse>

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get genre")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
        ApiResponse(responseCode = "404", description = "Genre not found"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun findById(
        @PathVariable id: String,
    ): GenreResponse

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete genre")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
        ApiResponse(responseCode = "404", description = "Genre not found"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun deleteById(@PathVariable id: String)
}