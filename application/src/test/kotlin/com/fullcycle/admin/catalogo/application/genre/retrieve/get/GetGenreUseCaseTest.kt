package com.fullcycle.admin.catalogo.application.genre.retrieve.get

import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreId
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times

class GetGenreUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultGetGenreByIdUseCase

    @Mock
    lateinit var genreGateway: GenreGateway

    override fun getMocks(): List<Any> {
        return listOf(genreGateway)
    }

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val aGenre = Genre.newGenre("Movies",true)

        val expectedId = aGenre.id
        val expectedName = aGenre.name
        val expectedCategoriesIds = aGenre.categoriesIds()
        val expectedIsActive = true

        whenever(genreGateway.findById(expectedId.value)).thenReturn(aGenre.copy())

        val actualGenre = useCase.execute(expectedId.value)

        Assertions.assertEquals(expectedName, aGenre.name)
        Assertions.assertEquals(expectedCategoriesIds, actualGenre.categoriesIds)
        Assertions.assertEquals(expectedIsActive, actualGenre.active)
        Assertions.assertEquals(expectedId.value, actualGenre.id)
        Assertions.assertEquals(aGenre.createdAt, actualGenre.createdAt)
        Assertions.assertEquals(aGenre.updatedAt, actualGenre.updatedAt)
        Assertions.assertEquals(aGenre.deletedAt, actualGenre.deletedAt)

    }

    @Test
    fun givenAnInValidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedId = GenreId.from("132")
        val expectedErrorMessage = "Genre with id ${expectedId.value} not found"
        val expectedErrorCount = 1

        whenever(genreGateway.findById(eq(expectedId.value))).thenReturn(null)

        val domainException = Assertions.assertThrows(DomainException::class.java) { useCase.execute(expectedId.value) }

        Assertions.assertEquals(expectedErrorCount, domainException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, domainException.message)
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldThrowException() {
        val aGenre = Genre.newGenre("Movies", true)
        val expectedId = aGenre.id
        val expectedErrorMessage = "Gateway exception"

        doThrow(IllegalStateException(expectedErrorMessage)).`when`(genreGateway).findById(eq(expectedId.value))

        val illegalStateException = Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(expectedId.value) }

        verify(genreGateway, times(1)).findById(eq(expectedId.value))

        Assertions.assertEquals(expectedErrorMessage, illegalStateException.message)
    }
}