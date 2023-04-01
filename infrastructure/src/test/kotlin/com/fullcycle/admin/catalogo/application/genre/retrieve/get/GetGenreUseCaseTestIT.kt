package com.fullcycle.admin.catalogo.application.genre.retrieve.get

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
internal class GetGenreUseCaseTestIT {

    @Autowired
    lateinit var useCase: DefaultGetGenreByIdUseCase

    @Autowired
    lateinit var genreGateway: GenreGateway

    @Autowired
    lateinit var genreRepository: GenreRepository


    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val aGenre = Genre.newGenre("Movies",true)

        val expectedId = aGenre.id
        val expectedName = aGenre.name
        val expectedCategoriesIds = aGenre.categoriesIds()
        val expectedIsActive = true

        Assertions.assertEquals(genreRepository.count(), 0)

        genreGateway.create(aGenre)

        Assertions.assertEquals(genreRepository.count(), 1)

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
        val aGenre = Genre.newGenre("Movies",true)
        val expectedId = GenreID.from("132")
        val expectedErrorMessage = "Genre with id ${expectedId.value} not found"
        val expectedErrorCount = 1

        Assertions.assertEquals(genreRepository.count(), 0)

        genreGateway.create(aGenre)

        Assertions.assertEquals(genreRepository.count(), 1)

        val domainException = Assertions.assertThrows(DomainException::class.java) { useCase.execute(expectedId.value) }

        Assertions.assertEquals(expectedErrorCount, domainException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, domainException.message)
    }
}