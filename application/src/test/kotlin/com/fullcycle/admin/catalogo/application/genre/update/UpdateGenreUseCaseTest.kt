package com.fullcycle.admin.catalogo.application.genre.update

import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import org.mockito.Mockito
import java.util.*

class UpdateGenreUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultUpdateGenreUseCase

    @Mock
    lateinit var genreGateway: GenreGateway

    @Mock
    lateinit var categoryGateway: CategoryGateway

    override fun getMocks(): List<Any> {
       return listOf(genreGateway, categoryGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreUpdated() {
        val genre = Genre.newGenre("Movie", true)
        val series = CategoryID.from("123")
        val documentaries = CategoryID.from("456")
        val expectedGenreId = genre.id.value
        val expectedName = "Documentary"
        val expectedIsActive = false
        val expectedErrorMessage = "Some categories could not be found: 123"
        val expectedErrorMessageCount = 1
        val expectedCategoriesIds = listOf(series, documentaries)

        whenever(categoryGateway.existsById(any())).thenReturn(listOf(documentaries))
        whenever(genreGateway.findById(expectedGenreId)).thenReturn(genre)

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        val notification = Assertions.assertThrows(NotificationException::class.java) { useCase.execute(aCommand) }.notification

        Assertions.assertEquals(notification.firstError()?.message, expectedErrorMessage)
        Assertions.assertEquals(notification.getErrors().size, expectedErrorMessageCount)

        verify(genreGateway, Mockito.times(0)).update(any())
        verify(categoryGateway, Mockito.times(1)).existsById(any())
    }

    @Test
    fun givenACategoryWith2CategoriesIds_whenCallsUpdateGenreWithZeroCategoriesIds_shouldReturnGenreWithZeroCategoriesIds() {
        val genre = Genre.newGenre("Movie", false)
        genre.addCategoriesIds(listOf(
            CategoryID.from("123"),
            CategoryID.from("456"),
        ))
        val expectedGenreId = genre.id.value
        val expectedName = "Documentary"
        val expectedIsActive = true
        val expectedCategoriesIds = listOf<CategoryID>()

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        whenever(genreGateway.findById(expectedGenreId)).thenReturn(genre)
        whenever(genreGateway.update(any())).thenAnswer { it.arguments[0] }

        val updateGenreOutput = useCase.execute(aCommand)

        Assertions.assertNotNull(updateGenreOutput)
        Assertions.assertEquals(updateGenreOutput.id, expectedGenreId)

        verify(genreGateway, times(1)).update(argThat { aGenre ->
            Objects.equals(expectedName, aGenre.name)
                    && Objects.equals(expectedIsActive, aGenre.active)
                    && Objects.equals(expectedCategoriesIds, aGenre.categoriesIds())
                    && Objects.nonNull(aGenre.id)
                    && Objects.nonNull(aGenre.createdAt)
                    && Objects.nonNull(aGenre.updatedAt)
                    && Objects.isNull(aGenre.deletedAt)
        })
        verify(genreGateway, times(1))
            .findById(argThat { aGenreIdStr ->
                Objects.equals(aGenreIdStr, expectedGenreId)
            })
    }

    @Test
    fun givenACategoryWithZeroCategoriesIds_whenCallsUpdateGenreWith2CategoriesIds_shouldReturnGenreWith2CategoriesIds() {
        val genre = Genre.newGenre("Movie", true)
        val expectedGenreId = genre.id.value
        val expectedName = "Documentary"
        val expectedIsActive = false
        val expectedCategoriesIds = listOf(
            CategoryID.from("123"),
            CategoryID.from("456"),
        )

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        whenever(genreGateway.findById(expectedGenreId)).thenReturn(genre)
        whenever(categoryGateway.existsById(any())).thenReturn(expectedCategoriesIds)
        whenever(genreGateway.update(any())).thenAnswer { it.arguments[0] }

        val updateGenreOutput = useCase.execute(aCommand)

        Assertions.assertNotNull(updateGenreOutput)
        Assertions.assertEquals(updateGenreOutput.id, expectedGenreId)

        verify(genreGateway, times(1)).update(argThat { aGenre ->
            Objects.equals(expectedName, aGenre.name)
                    && Objects.equals(expectedIsActive, aGenre.active)
                    && Objects.equals(expectedCategoriesIds, aGenre.categoriesIds())
                    && Objects.nonNull(aGenre.id)
                    && Objects.nonNull(aGenre.createdAt)
                    && Objects.nonNull(aGenre.updatedAt)
                    && Objects.nonNull(aGenre.deletedAt)
        })
        verify(genreGateway, times(1))
            .findById(argThat { aGenreIdStr ->
                Objects.equals(aGenreIdStr, expectedGenreId)
            })
    }

    @Test
    fun givenAValidCommand_whenGenreNotFound_shouldReturnNotFoundException() {
        val genre = Genre.newGenre("Movie", true)
        val expectedGenreId = genre.id.value
        val expectedName = genre.name
        val expectedIsActive = true
        val expectedCategoriesIds = listOf<CategoryID>()

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        whenever(genreGateway.findById(expectedGenreId)).thenThrow(NotFoundException::class.java)

        val notFoundException = Assertions.assertThrows(NotFoundException::class.java) { useCase.execute(aCommand) }

        Assertions.assertNotNull(notFoundException)

        verify(genreGateway, times(0)).update(any())
        verify(genreGateway, times(1))
            .findById(argThat { aGenreIdStr ->
                Objects.equals(aGenreIdStr, expectedGenreId)
            })
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

        val aCommand = UpdateGenreCommand.with(expectedGenreId, expectedName, expectedIsActive, asString(expectedCategoriesIds))

        whenever(genreGateway.findById(expectedGenreId)).thenReturn(genre)

        val notificationException = Assertions.assertThrows(NotificationException::class.java) { useCase.execute(aCommand) }

        Assertions.assertNotNull(notificationException)
        Assertions.assertEquals(notificationException.notification.getErrors().size, expectedErrorCount)
        Assertions.assertEquals(notificationException.message, expectedErrorMessage)

        verify(genreGateway, times(0)).update(any())
        verify(genreGateway, times(1))
            .findById(argThat { aGenreIdStr ->
                Objects.equals(aGenreIdStr, expectedGenreId)
            })
    }

    private fun asString(categoriesIds: List<CategoryID>): List<String> {
        return categoriesIds.map { it.value }
    }
}