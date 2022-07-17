package com.fullcycle.admin.catalogo.domain.genre

import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GenreTest {

    @Test
    fun givenAValidParams_whenCallNewGenre_thenInstantiateAGenre() {
        val expectedName = "new name"
        val expectedIsActive = true

        val genre = Genre.newGenre(expectedName, expectedIsActive)

        Assertions.assertNotNull(genre.id)
        Assertions.assertEquals(genre.name, expectedName)
        Assertions.assertEquals(genre.active, expectedIsActive)
        Assertions.assertEquals(genre.categoriesIds().size, 0)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertNotNull(genre.updatedAt)
        Assertions.assertNull(genre.deletedAt)
    }

    @Test
    fun givenAnInvalidEmptyName_whenCallNewGenre_thenShouldReceiveAnError() {
        val expectedName = ""
        val expectedErrorMessage = "'name' should not be null or empty"
        val expectedErrorMessagesCount = 1
        val expectedIsActive = true

        val actualException = Assertions.assertThrows(NotificationException::class.java) { Genre.newGenre(expectedName, expectedIsActive) }

        Assertions.assertEquals(expectedErrorMessagesCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
    }

    @Test
    fun givenANameLengthGreaterThan255_whenCallNewGenre_thenShouldReceiveAnError() {
        val expectedName = """
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
        """
        val expectedErrorMessage = "'name' must be between 1 and 255 characters"
        val expectedErrorMessagesCount = 1
        val expectedIsActive = true

        val actualException = Assertions.assertThrows(NotificationException::class.java) { Genre.newGenre(expectedName, expectedIsActive) }

        Assertions.assertEquals(expectedErrorMessagesCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
    }


}