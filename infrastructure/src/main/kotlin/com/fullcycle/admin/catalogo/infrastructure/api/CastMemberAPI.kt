package com.fullcycle.admin.catalogo.infrastructure.api

import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CastMemberResponse
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest
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


@RequestMapping("/cast_members")
@Tag(name = "Cast Members")
interface CastMemberAPI {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new cast member")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Created successfully"
        ), ApiResponse(
            responseCode = "422",
            description = "A validation error was thrown"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun create(@RequestBody input: CreateCastMemberRequest): ResponseEntity<*>

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "List all cast members")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Cast members retrieved"
        ), ApiResponse(
            responseCode = "500",
            description = "An internal server error was thrown"
        )]
    )
    fun list(
        @RequestParam(name = "search", required = false, defaultValue = "") search: String? = "",
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int? = 0,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") perPage: Int? = 10,
        @RequestParam(name = "sort", required = false, defaultValue = "name") sort: String? = "name",
        @RequestParam(name = "dir", required = false, defaultValue = "asc") direction: String? = "asc"
    ): Pagination<CastMemberListResponse>

    @GetMapping(value = ["{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get a cast member by it's identifier")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Cast member retrieved"
        ), ApiResponse(
            responseCode = "404",
            description = "Cast member was not found"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun getById(@PathVariable id: String): CastMemberResponse?

    @PutMapping(
        value = ["{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Update a cast member by it's identifier")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Cast member updated"
        ), ApiResponse(
            responseCode = "404",
            description = "Cast member was not found"
        ), ApiResponse(
            responseCode = "422",
            description = "A validation error was thrown"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun updateById(@PathVariable id: String, @RequestBody aBody: UpdateCastMemberRequest): ResponseEntity<*>

    @DeleteMapping(value = ["{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a cast member by it's identifier")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "204",
            description = "Cast member deleted"
        ), ApiResponse(
            responseCode = "500",
            description = "An internal server error was thrown"
        )]
    )
    fun deleteById(@PathVariable id: String)
}