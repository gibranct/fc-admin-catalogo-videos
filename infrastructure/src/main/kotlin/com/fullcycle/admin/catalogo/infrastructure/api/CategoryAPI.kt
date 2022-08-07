package com.fullcycle.admin.catalogo.infrastructure.api

import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryListResponse
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
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
    "/categories"
)
@Tag(name = "Categories")
interface CategoryAPI {

    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new category")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun create(@RequestBody input: CreateCategoryRequest): ResponseEntity<*>

    @PutMapping("/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Update a category")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Updated successfully"),
        ApiResponse(responseCode = "404", description = "Category not found"),
        ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun updateById(@PathVariable id: String, @RequestBody input: UpdateCategoryRequest): ResponseEntity<*>


    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "List categories paginated")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List categories successfully"),
        ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun list(
        @RequestParam() search: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") perPage: Int,
        @RequestParam(defaultValue = "name") sort: String,
        @RequestParam(name = "dir", defaultValue = "asc") direction: String,
    ): Pagination<CategoryListResponse>

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get category")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        ApiResponse(responseCode = "404", description = "Category not found"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun findById(
        @PathVariable id: String,
    ): CategoryResponse

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        ApiResponse(responseCode = "404", description = "Category not found"),
        ApiResponse(responseCode = "500", description = "An unexpected error was thrown"),
    ])
    fun deleteById(@PathVariable id: String)
}