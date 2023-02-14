package com.fullcycle.admin.catalogo.application.category.retrieve.list

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery
import com.fullcycle.admin.catalogo.infrastructure.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
internal class ListCategoriesUseCaseIT {

    @Autowired
    lateinit var listCategoryUseCase: DefaultListCategoriesUseCase

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    fun mockUp() {
        val categoryJpaEntities = listOf(
            Category.newCategory("Film", null, true),
            Category.newCategory("Netflix Originals", "Netflix original productions", true),
            Category.newCategory("Amazon Originals", "Amazon original productions", true),
            Category.newCategory("Documentary", null, true),
            Category.newCategory("Sports", null, true),
            Category.newCategory("Kids", "children", true),
            Category.newCategory("Series", null, true),
        ).map { CategoryJpaEntity.from(it) }

        categoryRepository.saveAll(categoryJpaEntities)
    }

    @Test
    fun givenAValidTerm_whenTermNotMatchesCategories_shouldReturnEmptyPage() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "asdasd asdsas"
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedItemsCount = 0
        val expectedTotal = 0L

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = listCategoryUseCase.execute(aQuery)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedTotal, actualResult.total)
    }

    @ParameterizedTest
    @MethodSource("getFilteredTestParameters")
    fun givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
        expectedTerms: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoryName: String,
    ) {
        val expectedSort = "name"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = listCategoryUseCase.execute(aQuery)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedCategoryName, actualResult.items.first().name)
    }

    @ParameterizedTest
    @MethodSource("getSortedTestParameters")
    fun givenAValidSortAndDirection_whenCallsListCategories_shouldReturnCategoriesSorted(
        expectedSort: String,
        expectedDirection: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoryName: String,
    ) {
        val expectedTerms = ""

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = listCategoryUseCase.execute(aQuery)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedCategoryName, actualResult.items.first().name)
    }

    @ParameterizedTest
    @MethodSource("getPaginatedTestParameters")
    fun givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoriesName: String,
    ) {
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTerms = ""

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = listCategoryUseCase.execute(aQuery)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedTotal, actualResult.total)

        var index = 0
        expectedCategoriesName.split(";").forEach {
            Assertions.assertEquals(it, actualResult.items[index++].name)
        }

    }

    companion object {
        @JvmStatic
        fun getFilteredTestParameters() = listOf(
            Arguments.of("fil", 0, 10, 1, 1, "Film"),
            Arguments.of("net", 0, 10, 1, 1, "Netflix Originals"),
            Arguments.of("ZON", 0, 10, 1, 1, "Amazon Originals"),
            Arguments.of("KI", 0, 10, 1, 1, "Kids"),
            Arguments.of("children", 0, 10, 1, 1, "Kids"),
            Arguments.of("Amazon original pro", 0, 10, 1, 1, "Amazon Originals"),
        )

        @JvmStatic
        fun getSortedTestParameters() = listOf(
            Arguments.of("name", "asc", 0, 10, 7 , 7, "Amazon Originals"),
            Arguments.of("name", "desc", 0, 10, 7 , 7, "Sports"),
            Arguments.of("createdAt", "asc", 0, 10, 7 , 7, "Film"),
            Arguments.of("createdAt", "desc", 0, 10, 7 , 7, "Series"),
        )

        @JvmStatic
        fun getPaginatedTestParameters() = listOf(
            Arguments.of(0, 2, 2, 7, "Amazon Originals;Documentary"),
            Arguments.of(1, 2, 2, 7, "Film;Kids"),
            Arguments.of(2, 2, 2, 7, "Netflix Originals;Series"),
            Arguments.of(3, 2, 1, 7, "Sports"),
        )
    }
}