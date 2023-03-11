package com.fullcycle.admin.catalogo.application.genre.delete

import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

class DeleteGenreUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultDeleteGenreUseCase

    @Mock
    lateinit var genreGateway: GenreGateway

    override fun getMocks(): List<Any> {
        return listOf(genreGateway)
    }

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        val genre = Genre.newGenre("Movies", true)
        val expectedId = genre.id

        doNothing().`when`(genreGateway).deleteById(eq(expectedId.value))

        useCase.execute(expectedId.value)

        verify(genreGateway, Mockito.times(1)).deleteById(eq(expectedId.value))
    }

    @Test
    fun givenAnInValidId_whenCallsDeleteCategory_shouldBeOk() {
        val expectedId = GenreID.from("123")

        doNothing().`when`(genreGateway).deleteById(eq(expectedId.value))

        useCase.execute(expectedId.value)

        verify(genreGateway, Mockito.times(1)).deleteById(eq(expectedId.value))
    }

    @Test
    fun givenAValidId_whenGatewayThrowException_shouldThrowException() {
        val genre = Genre.newGenre("Movies", true)
        val expectedId = genre.id

        doThrow(IllegalStateException("Gateway exception")).`when`(genreGateway).deleteById(eq(expectedId.value))

        Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(expectedId.value) }

        verify(genreGateway, Mockito.times(1)).deleteById(eq(expectedId.value))
    }
}