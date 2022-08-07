package com.fullcycle.admin.catalogo.application.genre.create

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.infrastructure.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
internal class CreateGenreUseCaseTestIT {

    @Autowired
    lateinit var useCase: DefaultCreateGenreUseCase

    @Autowired
    lateinit var genreGateway: GenreGateway

    @Autowired
    lateinit var categoryGateway: CategoryGateway

    @Autowired
    lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectedName = "valid name"
        val expectedIsActive = true
        val expectedCategoriesIds = listOf<CategoryID>()

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategoriesIds))

        Assertions.assertEquals(genreRepository.count(), 0)

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertEquals(genreRepository.count(), 1)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        val persistedGenre = genreGateway.findById(actualOutput.id)!!

        Assertions.assertEquals(persistedGenre.active, expectedIsActive)
        Assertions.assertEquals(persistedGenre.name, expectedName)
        Assertions.assertNotNull(persistedGenre.createdAt)
        Assertions.assertNotNull(persistedGenre.updatedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
        Assertions.assertTrue(persistedGenre.categoriesIds().isEmpty())
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        val movie = categoryGateway.create(Category.newCategory("movie", null, true))
        val documentary = categoryGateway.create(Category.newCategory("documentary", null, true))
        val expectedName = "valid name"
        val expectedIsActive = true
        val expectedCategoriesIds = listOf(movie.id, documentary.id)

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategoriesIds))

        Assertions.assertEquals(genreRepository.count(), 0)

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertEquals(genreRepository.count(), 1)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        val persistedGenre = genreGateway.findById(actualOutput.id)!!

        Assertions.assertEquals(persistedGenre.active, expectedIsActive)
        Assertions.assertEquals(persistedGenre.name, expectedName)
        Assertions.assertNotNull(persistedGenre.createdAt)
        Assertions.assertNotNull(persistedGenre.updatedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
        Assertions.assertFalse(persistedGenre.categoriesIds().isEmpty())
        Assertions.assertTrue(
            persistedGenre.categoriesIds().size == expectedCategoriesIds.size &&
            persistedGenre.categoriesIds().containsAll(expectedCategoriesIds)
        )
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnInactiveGenreId() {
        val expectedName = "valid name"
        val expectedCategoriesIdsList = listOf<CategoryID>()
        val expectedIsActive = false

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategoriesIdsList))

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        val persistedGenre = genreRepository.findById(actualOutput.id).get()

        Assertions.assertEquals(persistedGenre.active, expectedIsActive)
        Assertions.assertEquals(persistedGenre.name, expectedName)
        Assertions.assertNotNull(persistedGenre.createdAt)
        Assertions.assertNotNull(persistedGenre.updatedAt)
        Assertions.assertNotNull(persistedGenre.deletedAt)
        Assertions.assertTrue(persistedGenre.categories.isEmpty())
    }

    @Test
    fun givenAValidCommand_whenCategoryIdDoesNotExists_shouldReturnNotificationException() {
        val movies = categoryGateway.create(Category.newCategory("movies", null, true))
        val documentaries = Category.newCategory("documentaries", null, true)
        val series = Category.newCategory("series", null, true)
        val seriesId = series.id
        val documentariesId = documentaries.id
        val moviesId = movies.id
        val expectedErrorMessage = "Some categories could not be found: ${seriesId.value},${documentariesId.value}"
        val expectedErrorMessageCount = 1

        val aCommand = CreateGenreCommand.with("valid name", true, asString(listOf(moviesId, seriesId, documentariesId)))

        val notification = Assertions.assertThrows(NotificationException::class.java) { useCase.execute(aCommand) }.notification

        Assertions.assertEquals(notification.firstError()?.message, expectedErrorMessage)
        Assertions.assertEquals(notification.getErrors().size, expectedErrorMessageCount)
    }

    private fun asString(categoriesIds: List<CategoryID>): List<String> {
        return categoriesIds.map { it.value }
    }
}