package com.fullcycle.admin.catalogo.application.category.retrieve.list

import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

class ListCategoriesUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultListCategoriesUseCase

    @Mock
    lateinit var categoryGateway: CategoryGateway

    override fun getMocks(): List<Any> {
        return listOf(categoryGateway)
    }

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

    @Test
    fun givenAValidQuery_whenCallsListCategories_shouldReturnCategories() {
        var categories = listOf(
            Category.newCategory("film", null, true),
            Category.newCategory("tv show", null, true)
        )

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"


        val searchQuery =
            SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val pagination = Pagination(expectedPage, expectedPerPage, categories.size.toLong(), categories)

        val expectedItemsCount = 2
        val expectedResult = pagination.map(ListCategoriesOutput::from)

        whenever(categoryGateway.findAll(eq(searchQuery))).thenReturn(pagination)

        val actualResult = useCase.execute(searchQuery)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedResult, actualResult)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(categories.size.toLong(), actualResult.total)
    }

    @Test
    fun givenAValidQuery_whenHasNoResults_shouldReturnEmptyList() {
        var categories = listOf<Category>()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"


        val searchQuery =
            SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val pagination = Pagination(expectedPage, expectedPerPage, 0, categories)

        val expectedItemsCount = 0
        val expectedResult = pagination.map(ListCategoriesOutput::from)

        whenever(categoryGateway.findAll(eq(searchQuery))).thenReturn(pagination)

        val actualResult = useCase.execute(searchQuery)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedResult, actualResult)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(categories.size.toLong(), actualResult.total)
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_shouldThrowException() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway exception"

        val searchQuery =
            SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)


        whenever(categoryGateway.findAll(eq(searchQuery))).thenThrow(IllegalStateException(expectedErrorMessage))

        val illegalStateException = Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(searchQuery) }

        Assertions.assertEquals(expectedErrorMessage, illegalStateException.message)
    }
}