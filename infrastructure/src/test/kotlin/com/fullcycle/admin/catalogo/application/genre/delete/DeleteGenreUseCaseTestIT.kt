package com.fullcycle.admin.catalogo.application.genre.delete

import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreId
import com.fullcycle.admin.catalogo.infrastructure.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
internal class DeleteGenreUseCaseTestIT {

    @Autowired
    lateinit var useCase: DefaultDeleteGenreUseCase

    @Autowired
    lateinit var genreGateway: GenreGateway

    @Autowired
    lateinit var genreRepository: GenreRepository

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        val genre = Genre.newGenre("Movies", true)
        val expectedId = genre.id

        assertEquals(genreRepository.count(), 0)

        genreGateway.create(genre)

        assertEquals(genreRepository.count(), 1)

        useCase.execute(expectedId.value)

        assertEquals(genreRepository.count(), 0)
    }

    @Test
    fun givenAnInValidId_whenCallsDeleteCategory_shouldBeOk() {
        val genre = Genre.newGenre("Movies", true)
        val expectedId = GenreId.from("123")

        assertEquals(genreRepository.count(), 0)

        genreGateway.create(genre)

        assertEquals(genreRepository.count(), 1)

        useCase.execute(expectedId.value)

        assertEquals(genreRepository.count(), 1)
    }
}