package com.fullcycle.admin.catalogo.application.genre.update

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
internal class UpdateGenreUseCaseTestIT {

    @Autowired
    lateinit var useCase: DefaultUpdateGenreUseCase

    @Autowired
    lateinit var genreGateway: GenreGateway

    @Autowired
    lateinit var categoryGateway: CategoryGateway

    @Autowired
    lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreUpdated() {
        val genre = Genre.newGenre("Movie", true)
        val series = Category.newCategory("series", null, true)
        val documentaries = categoryGateway.create(Category.newCategory("documentaries", null, true))
        val seriesId = series.id
        val documentariesId = documentaries.id
        val expectedGenreId = genre.id.value
        val expectedName = "Documentary"
        val expectedIsActive = false
        val expectedErrorMessage = "Some categories could not be found: ${seriesId.value}"
        val expectedErrorMessageCount = 1
        val expectedCategoriesIds = listOf(seriesId, documentariesId)

        assertEquals(genreRepository.count(), 0)

        genreGateway.create(genre)

        assertEquals(genreRepository.count(), 1)

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        val notification = assertThrows(NotificationException::class.java) { useCase.execute(aCommand) }.notification

        assertEquals(notification.firstError()?.message, expectedErrorMessage)
        assertEquals(notification.getErrors().size, expectedErrorMessageCount)
    }

    @Test
    fun givenACategoryWith2CategoriesIds_whenCallsUpdateGenreWithZeroCategoriesIds_shouldReturnGenreWithZeroCategoriesIds() {
        val series = categoryGateway.create(Category.newCategory("series", null, true))
        val documentaries = categoryGateway.create(Category.newCategory("documentaries", null, true))
        val genre = Genre.newGenre("Movie", false)
        genre.addCategoriesIds(listOf(series.id, documentaries.id))
        val expectedGenreId = genre.id.value
        val expectedName = "Documentary"
        val expectedIsActive = true
        val expectedCategoriesIds = listOf<CategoryID>()

        assertEquals(genreRepository.count(), 0)

        genreGateway.create(genre.copy())

        assertEquals(genreRepository.count(), 1)

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        val updateGenreOutput = useCase.execute(aCommand)

        assertNotNull(updateGenreOutput)
        assertEquals(updateGenreOutput.id, expectedGenreId)

        val persistedGenre = genreGateway.findById(expectedGenreId)!!

        assertEquals(expectedName, persistedGenre.name)
        assertEquals(expectedIsActive, persistedGenre.active)
        assertTrue(persistedGenre.categoriesIds().size == expectedCategoriesIds.size)
        assertEquals(expectedName, persistedGenre.name)
        assertEquals(genre.createdAt, persistedGenre.createdAt)
        assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        assertNull(persistedGenre.deletedAt)
    }

    @Test
    fun givenACategoryWithZeroCategoriesIds_whenCallsUpdateGenreWith2CategoriesIds_shouldReturnGenreWith2CategoriesIds() {
        val series = categoryGateway.create(Category.newCategory("series", null, true))
        val documentaries = categoryGateway.create(Category.newCategory("documentaries", null, true))
        val genre = Genre.newGenre("Movie", true)
        val expectedGenreId = genre.id.value
        val expectedName = "Documentary"
        val expectedIsActive = false
        val expectedCategoriesIds = listOf(series.id, documentaries.id)

        assertEquals(genreRepository.count(), 0)

        genreGateway.create(genre.copy())

        assertEquals(genreRepository.count(), 1)

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        val updateGenreOutput = useCase.execute(aCommand)

        assertNotNull(updateGenreOutput)
        assertEquals(updateGenreOutput.id, expectedGenreId)

        val persistedGenre = genreGateway.findById(expectedGenreId)!!

        assertEquals(expectedName, persistedGenre.name)
        assertEquals(expectedIsActive, persistedGenre.active)
        assertTrue(
            persistedGenre.categoriesIds().size == expectedCategoriesIds.size &&
                    persistedGenre.categoriesIds().containsAll(expectedCategoriesIds)
        )
        assertEquals(expectedName, persistedGenre.name)
        assertEquals(genre.createdAt, persistedGenre.createdAt)
        assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        assertNotNull(persistedGenre.deletedAt)
    }

    @Test
    fun givenAValidCommand_whenGenreNotFound_shouldReturnNotFoundException() {
        val genre = Genre.newGenre("Movie", true)
        val expectedGenreID = GenreID.from("123").value
        val expectedName = genre.name
        val expectedIsActive = true
        val expectedCategoriesIds = listOf<CategoryID>()

        assertEquals(genreRepository.count(), 0)

        genreGateway.create(genre.copy())

        assertEquals(genreRepository.count(), 1)

        val aCommand = UpdateGenreCommand.with(expectedGenreID, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        val notFoundException = assertThrows(NotFoundException::class.java) { useCase.execute(aCommand) }

        assertNotNull(notFoundException)
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        val genre = Genre.newGenre("Movie", true)
        val expectedGenreId = genre.id.value
        val expectedName = ""
        val expectedIsActive = true
        val expectedCategoriesIds = listOf<CategoryID>()
        val expectedErrorMessage = "Could not update Aggregate Genre $expectedGenreId"
        val expectedErrorCount = 2

        assertEquals(genreRepository.count(), 0)

        genreGateway.create(genre.copy())

        assertEquals(genreRepository.count(), 1)

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        val notificationException = assertThrows(NotificationException::class.java) { useCase.execute(aCommand) }

        assertNotNull(notificationException)
        assertEquals(notificationException.notification.getErrors().size, expectedErrorCount)
        assertEquals(notificationException.message, expectedErrorMessage)
    }

    private fun asString(categoriesIds: List<CategoryID>): List<String> {
        return categoriesIds.map { it.value }
    }
}