package com.fullcycle.admin.catalogo.infrastructure.genre

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreId
import com.fullcycle.admin.catalogo.domain.pagination.SeachQuery
import com.fullcycle.admin.catalogo.infrastructure.MySQLGatewayTest
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired

@MySQLGatewayTest
internal class GenreMySQLGatewayTest {

    @Autowired
    lateinit var genreGateway: GenreGateway

    @Autowired
    lateinit var categoryGateway: CategoryGateway

    @Autowired
    lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidGenre_whenCallsCreateWithCategories_shouldReturnANewGenre() {
        val category = categoryGateway.create(Category.newCategory("any", null, true))
        val expectedCategoryID = category.id
        val expectedName = "Film"
        val expectedIsActive = true
        val expectedCategories = listOf(expectedCategoryID)

        val aGenre = Genre.newGenre(expectedName, expectedIsActive)
        aGenre.addCategoriesIds(expectedCategories)

        assertThat(0).isEqualTo(genreRepository.count())

        val actualGenre = genreGateway.create(aGenre)

        assertThat(1).isEqualTo(genreRepository.count())

        assertThat(aGenre).usingRecursiveComparison().isEqualTo(actualGenre)

        val persistedGenre = genreGateway.findById(actualGenre.id.value)

        assertThat(aGenre).usingRecursiveComparison().isEqualTo(persistedGenre)
    }

    @Test
    fun givenAValidGenre_whenCallsCreateWithNoCategories_shouldReturnANewGenre() {
        val expectedName = "Film"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()

        val aGenre = Genre.newGenre(expectedName, expectedIsActive)
        aGenre.addCategoriesIds(expectedCategories)

        val expectedId = aGenre.id.value

        assertThat(0).isEqualTo(genreRepository.count())

        val actualGenre = genreGateway.create(aGenre)

        assertThat(1).isEqualTo(genreRepository.count())

        assertThat(aGenre).usingRecursiveComparison().isEqualTo(actualGenre)

        val persistedGenre = genreGateway.findById(expectedId)

        assertThat(aGenre).usingRecursiveComparison().isEqualTo(persistedGenre)
    }

    @Test
    fun givenAValidGenreWithCategories_whenCallsUpdateCleaningCategories_shouldReturnAGenreWithNoCategories() {
        val category = categoryGateway.create(Category.newCategory("any", null, true))
        val expectedCategoryID = category.id
        val expectedName = "Film"
        val expectedIsActive = true
        val expectedCategories = listOf(expectedCategoryID)

        val aGenre = Genre.newGenre("any", true)
        aGenre.addCategoriesIds(expectedCategories)

        assertThat(0).isEqualTo(genreRepository.count())

        genreGateway.create(aGenre.copy())

        assertThat(1).isEqualTo(genreRepository.count())

        val aGenreToUpdate = aGenre.copy().update(expectedName, expectedIsActive, listOf())

        val actualGenre = genreGateway.update(aGenreToUpdate)

        assertThat(1).isEqualTo(genreRepository.count())

        assertThat(actualGenre.id.value).isEqualTo(aGenre.id.value)
        assertThat(actualGenre.name).isEqualTo(expectedName)
        assertThat(actualGenre.active).isEqualTo(expectedIsActive)
        assertThat(actualGenre.categoriesIds().size).isZero
        assertThat(actualGenre.createdAt).isEqualTo(aGenre.createdAt)
        assertThat(actualGenre.updatedAt.isAfter(aGenre.updatedAt)).isTrue
        assertThat(actualGenre.deletedAt).isNull()

        val persistedGenre = genreGateway.findById(actualGenre.id.value)

        assertThat(persistedGenre?.name).isEqualTo(expectedName)
        assertThat(persistedGenre?.active).isEqualTo(expectedIsActive)
        assertThat(persistedGenre?.categoriesIds()?.size).isZero
        assertThat(persistedGenre?.createdAt).isEqualTo(aGenre.createdAt)
        assertThat(persistedGenre?.updatedAt?.isAfter(aGenre.updatedAt)).isTrue
        assertThat(persistedGenre?.deletedAt).isNull()
    }

    @Test
    fun givenAValidGenreWithNoCategories_whenCallsUpdateWithCategories_shouldReturnAGenreWithCategories() {
        val category = categoryGateway.create(Category.newCategory("any", null, true))
        val expectedCategoryID = category.id
        val expectedName = "Film"
        val expectedIsActive = true
        val expectedCategories = listOf(expectedCategoryID)
        val expectedCategoriesCount = 1

        val aGenre = Genre.newGenre("any", true)

        assertThat(0).isEqualTo(genreRepository.count())

        genreGateway.create(aGenre.copy())

        assertThat(1).isEqualTo(genreRepository.count())

        val aGenreToUpdate = aGenre.copy().update(expectedName, expectedIsActive, listOf())
        aGenreToUpdate.addCategoriesIds(expectedCategories)

        val actualGenre = genreGateway.update(aGenreToUpdate)

        assertThat(1).isEqualTo(genreRepository.count())

        assertThat(actualGenre.id.value).isEqualTo(aGenre.id.value)
        assertThat(actualGenre.name).isEqualTo(expectedName)
        assertThat(actualGenre.active).isEqualTo(expectedIsActive)
        assertThat(actualGenre.categoriesIds().size).isEqualTo(expectedCategoriesCount)
        assertThat(actualGenre.categoriesIds().first()).isEqualTo(expectedCategoryID)
        assertThat(actualGenre.createdAt).isEqualTo(aGenre.createdAt)
        assertThat(actualGenre.updatedAt.isAfter(aGenre.updatedAt)).isTrue
        assertThat(actualGenre.deletedAt).isNull()

        val persistedGenre = genreGateway.findById(actualGenre.id.value)

        assertThat(persistedGenre?.name).isEqualTo(expectedName)
        assertThat(persistedGenre?.active).isEqualTo(expectedIsActive)
        assertThat(persistedGenre?.categoriesIds()?.size).isEqualTo(expectedCategoriesCount)
        assertThat(persistedGenre?.categoriesIds()?.first()).isEqualTo(expectedCategoryID)
        assertThat(persistedGenre?.createdAt).isEqualTo(aGenre.createdAt)
        assertThat(persistedGenre?.updatedAt?.isAfter(aGenre.updatedAt)).isTrue
        assertThat(persistedGenre?.deletedAt).isNull()
    }

    @Test
    fun givenAValidId_whenCallsGetGenreById_shouldReturnAGenre() {
        val aGenre = Genre.newGenre("any", true)
        val expectedGenreId = aGenre.id
        val expectedName = aGenre.name
        val expectedIsActive = aGenre.active
        val expectedCategoriesCount = 0

        assertThat(0).isEqualTo(genreRepository.count())

        genreGateway.create(aGenre.copy())

        assertThat(1).isEqualTo(genreRepository.count())

        val persistedGenre = genreGateway.findById(expectedGenreId.value)

        assertThat(persistedGenre?.name).isEqualTo(expectedName)
        assertThat(persistedGenre?.active).isEqualTo(expectedIsActive)
        assertThat(persistedGenre?.categoriesIds()?.size).isEqualTo(expectedCategoriesCount)
        assertThat(persistedGenre?.createdAt).isEqualTo(aGenre.createdAt)
        assertThat(persistedGenre?.updatedAt).isEqualTo(aGenre.updatedAt)
        assertThat(persistedGenre?.deletedAt).isNull()
    }

    @Test
    fun givenAnInValidId_whenCallsGetGenreById_shouldReturnNull() {
        val aGenre = Genre.newGenre("any", true)
        val expectedGenreId = GenreId.from("123")

        assertThat(0).isEqualTo(genreRepository.count())

        genreGateway.create(aGenre.copy())

        assertThat(1).isEqualTo(genreRepository.count())

        val persistedGenre = genreGateway.findById(expectedGenreId.value)

        assertThat(persistedGenre).isNull()
    }

    @Test
    fun givenAValidId_whenCallsDeleteById_shouldRemoveGenre() {
        val aGenre = Genre.newGenre("any", true)

        assertThat(0).isEqualTo(genreRepository.count())

        genreGateway.create(aGenre.copy())

        assertThat(1).isEqualTo(genreRepository.count())

        genreGateway.deleteById(aGenre.id.value)

        assertThat(0).isEqualTo(genreRepository.count())
    }

    @Test
    fun givenAnInValidId_whenCallsDeleteById_shouldBeOk() {
        val aGenre = Genre.newGenre("any", true)

        assertThat(0).isEqualTo(genreRepository.count())

        genreGateway.create(aGenre.copy())

        assertThat(1).isEqualTo(genreRepository.count())

        genreGateway.deleteById("invalid id")

        assertThat(1).isEqualTo(genreRepository.count())
    }

    @ParameterizedTest
    @MethodSource("getPaginatedTestParameters")
    fun givenPrePersistedGenres_whenCallsFindAll_shouldReturnPaginated(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedSize: Int,
        expectedTotal: Long
    ) {
        mockGenres()

        val query = SeachQuery(expectedPage, expectedPerPage, "", "name", "asc")
        val actualResult = genreGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.perPage).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
        assertThat(actualResult.items.size).isEqualTo(expectedSize)
    }

    @Test
    fun givenPrePersistedGenres_whenCallsFindAll_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 5
        val expectedTotal = 1L
        val expectedSize = 1
        val expectedGenreName = "horror"

        mockGenres()

        val query = SeachQuery(expectedPage, expectedPerPage, "ho", "name", "asc")
        val actualResult = genreGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.perPage).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
        assertThat(actualResult.items.size).isEqualTo(expectedSize)
        assertThat(actualResult.items.first().name).isEqualTo(expectedGenreName)
    }

    @Test
    fun givenEmptyTableGenres_whenCallsFindAll_shouldReturnEmptyPage() {
        val expectedPage = 0
        val expectedPerPage = 0
        val expectedTotal = 0L

        val query = SeachQuery(0, 1, "", "name", "asc")
        val actualResult = genreGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.items.size).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
    }

    private fun mockGenres() {
        val action = Genre.newGenre("action", true)
        val drama = Genre.newGenre("drama", true)
        val comedy = Genre.newGenre("comedy", true)
        val fantasy = Genre.newGenre("fantasy", true)
        val horror = Genre.newGenre("horror", true)

        assertThat(genreRepository.count()).isEqualTo(0)

        genreRepository.saveAll(listOf(
            GenreJpaEntity.from(action),
            GenreJpaEntity.from(drama),
            GenreJpaEntity.from(comedy),
            GenreJpaEntity.from(fantasy),
            GenreJpaEntity.from(horror),
        ))

        assertThat(genreRepository.count()).isEqualTo(5)
    }

    companion object {
        @JvmStatic
        fun getPaginatedTestParameters() = listOf(
            Arguments.of(0, 2, 2, 5, "action;comedy"),
            Arguments.of(1, 2, 2, 5, "drama;fantasy"),
            Arguments.of(2, 2, 1, 5, "horror"),
        )
    }
}