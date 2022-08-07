package com.fullcycle.admin.catalogo.application.genre.create

import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

class CreateGenreUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultCreateGenreUseCase

    @Mock
    lateinit var genreGateway: GenreGateway

    @Mock
    lateinit var categoryGateway: CategoryGateway

    override fun getMocks(): List<Any> {
       return listOf(genreGateway, categoryGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectedName = "valid name"
        val expectedIsActive = true
        val expectedCategoriesIds = listOf<CategoryID>()

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategoriesIds))

        whenever(genreGateway.create(any())).thenAnswer {
            it.arguments[0]
        }

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        verify(genreGateway, times(1))
            .create(argThat { aGenre ->
                Objects.equals(expectedName, aGenre.name)
                        && Objects.equals(expectedIsActive, aGenre.active)
                        && Objects.nonNull(aGenre.id)
                        && Objects.nonNull(aGenre.createdAt)
                        && Objects.nonNull(aGenre.updatedAt)
                        && Objects.isNull(aGenre.deletedAt)
            })
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectedName = "valid name"
        val expectedIsActive = true
        val expectedCategoriesIds = listOf(
            CategoryID.from("123"),
            CategoryID.from("456")
        )

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategoriesIds))

        whenever(categoryGateway.existsById(any())).thenReturn(expectedCategoriesIds)

        whenever(genreGateway.create(any())).thenAnswer {
            it.arguments[0]
        }

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        verify(categoryGateway, times(1)).existsById(expectedCategoriesIds)

        verify(genreGateway, times(1))
            .create(argThat { aGenre ->
                Objects.equals(expectedName, aGenre.name)
                        && Objects.equals(expectedIsActive, aGenre.active)
                        && Objects.nonNull(aGenre.id)
                        && Objects.nonNull(aGenre.createdAt)
                        && Objects.nonNull(aGenre.updatedAt)
                        && Objects.isNull(aGenre.deletedAt)
            })
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnInactiveGenreId() {
        val expectedName = "valid name"
        val expectedCategoriesIdsList = listOf<CategoryID>()
        val expectedIsActive = false

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategoriesIdsList))

        whenever(genreGateway.create(any())).thenAnswer {
            it.arguments[0]
        }

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        verify(genreGateway, times(1))
            .create(argThat { aGenre ->
                Objects.equals(expectedName, aGenre.name)
                        && Objects.equals(expectedIsActive, aGenre.active)
                        && Objects.equals(expectedCategoriesIdsList, aGenre.categoriesIds())
                        && Objects.nonNull(aGenre.id)
                        && Objects.nonNull(aGenre.createdAt)
                        && Objects.nonNull(aGenre.updatedAt)
                        && Objects.nonNull(aGenre.deletedAt)
            })
    }

    @Test
    fun givenAValidCommand_whenGatewayThrows_shouldReturnAnException() {
        val expectedErrorMessage = "Gateway exception"

        val aCommand = CreateGenreCommand.with("valid name", true, listOf())

        whenever(genreGateway.create(any())).thenThrow(IllegalArgumentException("Gateway exception"))

        val illegalArgumentException = Assertions.assertThrows(IllegalArgumentException::class.java) { useCase.execute(aCommand) }

        Assertions.assertEquals(illegalArgumentException.message, expectedErrorMessage)

        verify(genreGateway, times(1)).create(any())
    }

    @Test
    fun givenAValidCommand_whenCategoryIdDoesNotExists_shouldReturnNotificationException() {

        val series = CategoryID.from("123")
        val documentaries = CategoryID.from("456")
        val movies = CategoryID.from("789")
        val expectedErrorMessage = "Some categories could not be found: 123,456"
        val expectedErrorMessageCount = 1
        val expectedCategoriesIdsList = listOf(series, documentaries, movies)

        val aCommand = CreateGenreCommand.with("valid name", true, asString(expectedCategoriesIdsList))

        whenever(categoryGateway.existsById(any())).thenReturn(listOf(movies))

        val notification = Assertions.assertThrows(NotificationException::class.java) { useCase.execute(aCommand) }.notification

        Assertions.assertEquals(notification.firstError()?.message, expectedErrorMessage)
        Assertions.assertEquals(notification.getErrors().size, expectedErrorMessageCount)

        verify(genreGateway, times(0)).create(any())
        verify(categoryGateway, times(1)).existsById(any())
    }

    @ParameterizedTest
    @MethodSource("invalidNamesAndMessages")
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnADomainException(expectedName: String, expectedErrorMessage: String, expectedErrorCount: Int) {
        val expectedIsActive = true

        val aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, listOf())

        val actualException = Assertions.assertThrows(NotificationException::class.java) { useCase.execute(aCommand) }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(actualException.notification.getErrors().size, expectedErrorCount)
        Assertions.assertEquals(actualException.notification.firstError()?.message, expectedErrorMessage)

        verify(genreGateway, times(0)).create(any())
        verify(categoryGateway, times(0)).create(any())
    }

    companion object {
        private val STRING_GREATER_THAN_255 = "A".padEnd(256, 'B')

        @JvmStatic
        fun invalidNamesAndMessages() = listOf(
            Arguments.of("", "'name' should not be null or empty", 2),
            Arguments.of(STRING_GREATER_THAN_255, "'name' must be between 1 and 255 characters", 1)
        )
    }

    private fun asString(categoriesIds: List<CategoryID>): List<String> {
        return categoriesIds.map { it.value }
    }
}