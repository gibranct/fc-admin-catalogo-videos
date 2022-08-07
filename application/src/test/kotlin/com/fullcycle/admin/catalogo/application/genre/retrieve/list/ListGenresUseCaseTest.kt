package com.fullcycle.admin.catalogo.application.genre.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class ListGenresUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultListGenresUseCase

    @Mock
    lateinit var genreGateway: GenreGateway

    override fun getMocks(): List<Any> {
        return listOf(genreGateway)
    }

    @Test
    fun givenAValidQuery_whenCallsListCategories_shouldReturnCategories() {
        val genres = listOf(
            Genre.newGenre("Movies", true),
            Genre.newGenre("Documentaries", true)
        )

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"

        val searchQuery =
            SeachQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val pagination = Pagination(expectedPage, expectedPerPage, genres.size.toLong(), genres)

        val expectedItemsCount = 2
        val expectedResult = pagination.map(ListGenresOutput::from)

        whenever(genreGateway.findAll(eq(searchQuery))).thenReturn(pagination)

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
            SeachQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val pagination = Pagination(expectedPage, expectedPerPage, 0, listOf<Genre>())

        val expectedItemsCount = 0
        val expectedResult = pagination.map(ListGenresOutput::from)

        whenever(genreGateway.findAll(eq(searchQuery))).thenReturn(pagination)

        val actualResult = useCase.execute(searchQuery)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedResult, actualResult)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedSize.toLong(), actualResult.total)
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_shouldThrowException() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway exception"

        val seachQuery =
            SeachQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)


        whenever(genreGateway.findAll(eq(seachQuery))).thenThrow(IllegalStateException(expectedErrorMessage))

        val illegalStateException = Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(seachQuery) }

        Assertions.assertEquals(expectedErrorMessage, illegalStateException.message)
    }
}