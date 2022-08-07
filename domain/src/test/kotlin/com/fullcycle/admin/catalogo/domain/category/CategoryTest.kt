package com.fullcycle.admin.catalogo.domain.category

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CategoryTest {

    @Test
    fun givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        val expectedName = "new name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertNotNull(category.id)
        Assertions.assertEquals(category.name, expectedName)
        Assertions.assertEquals(category.description, expectedDescription)
        Assertions.assertEquals(category.isActive, expectedIsActive)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertNotNull(category.updatedAt)
        Assertions.assertNull(category.deletedAt)
    }

    @Test
    fun givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveAnError() {
        val expectedName = ""
        val expectedErrorMessage = "'name' should not be null or empty"
        val expectedErrorMessagesCount = 1
        val expectedDescription = "new description"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        val actualException = Assertions.assertThrows(DomainException::class.java) { category.validate(ThrowsValidationHandler()) }

        Assertions.assertEquals(expectedErrorMessagesCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
    }

    @Test
    fun givenANameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveAnError() {
        val expectedName = "fs "
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val expectedErrorMessagesCount = 1
        val expectedDescription = "new description"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        val actualException = Assertions.assertThrows(DomainException::class.java) { category.validate(ThrowsValidationHandler()) }

        Assertions.assertEquals(expectedErrorMessagesCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
    }

    @Test
    fun givenANameLengthGreaterThan255_whenCallNewCategoryAndValidate_thenShouldReceiveAnError() {
        val expectedName = """
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
            É claro que a contínua expansão de nossa atividade promove a alavancagem da gestão inovadora da qual fazemos parte.
        """
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val expectedErrorMessagesCount = 1
        val expectedDescription = "new description"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        val actualException = Assertions.assertThrows(DomainException::class.java) { category.validate(ThrowsValidationHandler()) }

        Assertions.assertEquals(expectedErrorMessagesCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
    }

    @Test
    fun givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldNotReceiveAnError() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = false

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertDoesNotThrow { category.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(category.id)
        Assertions.assertEquals(category.name, expectedName)
        Assertions.assertEquals(category.description, expectedDescription)
        Assertions.assertEquals(category.isActive, expectedIsActive)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertNotNull(category.updatedAt)
        Assertions.assertNotNull(category.deletedAt)
    }

    @Test
    fun givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        val updatedAt = category.updatedAt

        Assertions.assertDoesNotThrow { category.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(category.id)
        Assertions.assertEquals(category.name, expectedName)
        Assertions.assertEquals(category.description, expectedDescription)
        Assertions.assertEquals(category.isActive, expectedIsActive)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertNotNull(category.updatedAt)
        Assertions.assertNull(category.deletedAt)

        val actualCategory = category.deactivate()

        Assertions.assertDoesNotThrow { category.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(category.id)
        Assertions.assertEquals(category.name, expectedName)
        Assertions.assertEquals(category.description, expectedDescription)
        Assertions.assertEquals(actualCategory.isActive, false)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt))
        Assertions.assertNotNull(category.deletedAt)
    }

    @Test
    fun givenAValidActiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = false

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        val updatedAt = category.updatedAt

        Assertions.assertDoesNotThrow { category.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(category.id)
        Assertions.assertEquals(category.name, expectedName)
        Assertions.assertEquals(category.description, expectedDescription)
        Assertions.assertEquals(category.isActive, expectedIsActive)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertNotNull(category.updatedAt)
        Assertions.assertNotNull(category.deletedAt)

        val actualCategory = category.activate()

        Assertions.assertDoesNotThrow { category.validate(ThrowsValidationHandler()) }

        Assertions.assertNotNull(category.id)
        Assertions.assertEquals(category.name, expectedName)
        Assertions.assertEquals(category.description, expectedDescription)
        Assertions.assertEquals(actualCategory.isActive, true)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(category.deletedAt)
    }

    @Test
    fun givenAValidActivatedCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = false

        val category = Category.newCategory("valid name", "valid desc", true)
        Assertions.assertTrue(category.isActive)
        Assertions.assertNull(category.deletedAt)

        val updatedAt = category.updatedAt

        val actualCategory = category.update(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertNotNull(actualCategory.id)
        Assertions.assertEquals(actualCategory.name, expectedName)
        Assertions.assertEquals(actualCategory.description, expectedDescription)
        Assertions.assertFalse(actualCategory.isActive)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt))
        Assertions.assertNotNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAValidInactivatedCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        val category = Category.newCategory("valid name", "valid desc", false)
        Assertions.assertFalse(category.isActive)
        Assertions.assertNotNull(category.deletedAt)

        val updatedAt = category.updatedAt

        val actualCategory = category.update(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertNotNull(actualCategory.id)
        Assertions.assertEquals(actualCategory.name, expectedName)
        Assertions.assertEquals(actualCategory.description, expectedDescription)
        Assertions.assertTrue(actualCategory.isActive)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAnInvalidInactivatedCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        val expectedName = "va"
        val expectedDescription = "new description"
        val expectedIsActive = true

        val category = Category.newCategory("valid name", "valid desc", false)
        Assertions.assertFalse(category.isActive)
        Assertions.assertNotNull(category.deletedAt)

        val updatedAt = category.updatedAt

        val actualCategory = category.update(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertNotNull(actualCategory.id)
        Assertions.assertEquals(actualCategory.name, expectedName)
        Assertions.assertEquals(actualCategory.description, expectedDescription)
        Assertions.assertTrue(actualCategory.isActive)
        Assertions.assertNotNull(category.createdAt)
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(actualCategory.deletedAt)
    }
}