package com.fullcycle.admin.catalogo.application.genre.retrieve.list

import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.IntegrationTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ListGenresUseCaseTestIT {

    @Autowired
    lateinit var useCase: DefaultListGenresUseCase

    @Autowired
    lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidQuery_whenCallsListCategories_shouldReturnCategories() {
        val movies = genreGateway.create(Genre.newGenre("Movies", true))
        val documentaries = genreGateway.create(Genre.newGenre("Documentaries", true))
        val genres = listOf(movies, documentaries)

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"

        val searchQuery =
            SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val pagination = Pagination(expectedPage, expectedPerPage, genres.size.toLong(), genres)

        val expectedItemsCount = 2
        val expectedResult = pagination.map(ListGenresOutput::from)

        val actualResult = useCase.execute(searchQuery)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedResult, actualResult)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(genres.size.toLong(), actualResult.total)
    }

    @Test
    fun givenAValidQuery_whenHasNoResults_shouldReturnEmptyList() {
        val expectedPage = 0
        val expectedSize = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"

        val searchQuery =
            SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val pagination = Pagination(expectedPage, expectedPerPage, 0, listOf<Genre>())

        val expectedItemsCount = 0
        val expectedResult = pagination.map(ListGenresOutput::from)

        val actualResult = useCase.execute(searchQuery)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedResult, actualResult)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedSize.toLong(), actualResult.total)
    }
}