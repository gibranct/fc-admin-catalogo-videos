package com.fullcycle.admin.catalogo.domain.genre

import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler
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
        val expectedErrorMessagesCount = 2
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

    @Test
    fun givenAValidFalseActive_whenCallNewGenreAndValidate_thenShouldNotReceiveAnError() {
        val expectedName = "valid name"
        val expectedIsActive = false

        val genre = Genre.newGenre(expectedName, expectedIsActive)

        Assertions.assertDoesNotThrow { genre.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(genre.id)
        Assertions.assertEquals(genre.name, expectedName)
        Assertions.assertEquals(genre.active, expectedIsActive)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertNotNull(genre.updatedAt)
        Assertions.assertNotNull(genre.deletedAt)
    }

    @Test
    fun givenAValidActiveGenre_whenCallDeactivate_thenReturnGenreInactivated() {
        val expectedName = "valid name"
        val expectedIsActive = true

        val genre = Genre.newGenre(expectedName, expectedIsActive)

        val updatedAt = genre.updatedAt

        Assertions.assertDoesNotThrow { genre.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(genre.id)
        Assertions.assertEquals(genre.name, expectedName)
        Assertions.assertEquals(genre.active, expectedIsActive)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertNotNull(genre.updatedAt)
        Assertions.assertNull(genre.deletedAt)

        val actualGenre = genre.deactivate()

        Assertions.assertDoesNotThrow { genre.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(genre.id)
        Assertions.assertEquals(genre.name, expectedName)
        Assertions.assertEquals(actualGenre.active, false)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertTrue(actualGenre.updatedAt.isAfter(updatedAt))
        Assertions.assertNotNull(genre.deletedAt)
    }

    @Test
    fun givenAValidActiveGenre_whenCallActivate_thenReturnGenreActivated() {
        val expectedName = "valid name"
        val expectedIsActive = false

        val genre = Genre.newGenre(expectedName, expectedIsActive)

        val updatedAt = genre.updatedAt

        Assertions.assertDoesNotThrow { genre.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(genre.id)
        Assertions.assertEquals(genre.name, expectedName)
        Assertions.assertEquals(genre.active, expectedIsActive)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertNotNull(genre.updatedAt)
        Assertions.assertNotNull(genre.deletedAt)

        val actualGenre = genre.activate()

        Assertions.assertDoesNotThrow { genre.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(genre.id)
        Assertions.assertEquals(genre.name, expectedName)
        Assertions.assertEquals(actualGenre.active, true)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertTrue(actualGenre.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(genre.deletedAt)
    }

    @Test
    fun givenAValidActivatedGenre_whenCallUpdate_thenReturnGenreUpdated() {
        val expectedName = "valid name"
        val expectedIsActive = false
        val expectedCategoryIds = mutableListOf(CategoryID.from("123"))

        val genre = Genre.newGenre("valid name", true)
        Assertions.assertTrue(genre.active)
        Assertions.assertNull(genre.deletedAt)
        Assertions.assertEquals(genre.categoriesIds().size, 0)

        val updatedAt = genre.updatedAt

        val actualGenre = genre.update(expectedName, expectedIsActive, expectedCategoryIds)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(actualGenre.name, expectedName)
        Assertions.assertFalse(actualGenre.active)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertTrue(actualGenre.updatedAt.isAfter(updatedAt))
        Assertions.assertNotNull(actualGenre.deletedAt)
        Assertions.assertEquals(actualGenre.categoriesIds(), expectedCategoryIds)
    }

    @Test
    fun givenAValidInactivatedGenre_whenCallUpdate_thenReturnGenreUpdated() {
        val expectedName = "valid name"
        val expectedIsActive = true
        val expectedCategoryIds = mutableListOf(CategoryID.from("123"))

        val genre = Genre.newGenre("valid name", false)
        Assertions.assertFalse(genre.active)
        Assertions.assertNotNull(genre.deletedAt)
        Assertions.assertEquals(genre.categoriesIds().size, 0)

        val updatedAt = genre.updatedAt

        val actualGenre = genre.update(expectedName, expectedIsActive, expectedCategoryIds)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(actualGenre.name, expectedName)
        Assertions.assertTrue(actualGenre.active)
        Assertions.assertNotNull(genre.createdAt)
        Assertions.assertTrue(actualGenre.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
        Assertions.assertEquals(actualGenre.categoriesIds(), expectedCategoryIds)
    }

    @Test
    fun givenAnInvalidInactivatedGenre_whenCallUpdate_thenThrowNotificationException() {
        val expectedName = ""
        val expectedIsActive = true
        val expectedCategoryIds = mutableListOf(CategoryID.from("123"))
        val expectedErrorMessage = "'name' should not be null or empty"
        val expectedErrorMessagesCount = 2

        val genre = Genre.newGenre("valid name", false)
        Assertions.assertFalse(genre.active)
        Assertions.assertNotNull(genre.deletedAt)
        Assertions.assertEquals(genre.categoriesIds().size, 0)

        val updatedAt = genre.updatedAt

        val notificationException = Assertions.assertThrowsExactly(NotificationException::class.java) {
            genre.update(
                expectedName,
                expectedIsActive,
                expectedCategoryIds
            )
        }

        Assertions.assertEquals(expectedErrorMessagesCount, notificationException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, notificationException.errors.first().message)
    }

    @Test
    fun givenAGenreWithZeroCategories_whenCallAddCategoryId_thenShouldReturnAGenreWith2Categories() {
        val categoryID123 = CategoryID.from("123")
        val categoryID456 = CategoryID.from("456")
        val expectedCategoryIds = mutableListOf(categoryID123, categoryID456)
        val expectedCategoryIdsCount = 2
        val expectedName = "valid name"

        val actualGenre = Genre.newGenre(expectedName, true)
        Assertions.assertEquals(actualGenre.categoriesIds().size, 0)
        val updatedAt = actualGenre.updatedAt

        actualGenre.addCategoryId(categoryID123)
        actualGenre.addCategoryId(categoryID456)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(actualGenre.name, expectedName)
        Assertions.assertTrue(actualGenre.active)
        Assertions.assertNotNull(actualGenre.createdAt)
        Assertions.assertTrue(actualGenre.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
        Assertions.assertEquals(actualGenre.categoriesIds(), expectedCategoryIds)
        Assertions.assertEquals(actualGenre.categoriesIds().size, expectedCategoryIdsCount)
    }

    @Test
    fun givenAGenreWith2Categories_whenCallRemoveCategoryId_thenShouldReturnAGenreWith1Category() {
        val categoryID123 = CategoryID.from("123")
        val categoryID456 = CategoryID.from("456")
        val expectedCategoryIds = mutableListOf(categoryID456)
        val expectedCategoryIdsCount = 1
        val expectedName = "valid name"

        val actualGenre = Genre.newGenre(expectedName, true)
        actualGenre.addCategoryId(categoryID123)
        actualGenre.addCategoryId(categoryID456)
        Assertions.assertEquals(actualGenre.categoriesIds().size, 2)
        val updatedAt = actualGenre.updatedAt

        actualGenre.removeCategoryId(categoryID123)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(actualGenre.name, expectedName)
        Assertions.assertTrue(actualGenre.active)
        Assertions.assertNotNull(actualGenre.createdAt)
        Assertions.assertTrue(actualGenre.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
        Assertions.assertEquals(actualGenre.categoriesIds(), expectedCategoryIds)
        Assertions.assertEquals(actualGenre.categoriesIds().size, expectedCategoryIdsCount)
    }

    @Test
    fun givenAGenreWithZeroCategories_whenCallAddCategoriesIds_thenShouldReturnAGenreWith2Categories() {
        val categoryID123 = CategoryID.from("123")
        val categoryID456 = CategoryID.from("456")
        val expectedCategoryIds = mutableListOf(categoryID123, categoryID456)
        val expectedCategoryIdsCount = 2
        val expectedName = "valid name"

        val actualGenre = Genre.newGenre(expectedName, true)
        Assertions.assertEquals(actualGenre.categoriesIds().size, 0)
        val updatedAt = actualGenre.updatedAt

        actualGenre.addCategoriesIds(expectedCategoryIds)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(actualGenre.name, expectedName)
        Assertions.assertTrue(actualGenre.active)
        Assertions.assertNotNull(actualGenre.createdAt)
        Assertions.assertTrue(actualGenre.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
        Assertions.assertEquals(actualGenre.categoriesIds(), expectedCategoryIds)
        Assertions.assertEquals(actualGenre.categoriesIds().size, expectedCategoryIdsCount)
    }
}