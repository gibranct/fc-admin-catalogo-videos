package com.fullcycle.admin.catalogo.application.category.retrieve.get

import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times

class GetCategoryUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultGetCategoryByIdUseCase

    @Mock
    lateinit var categoryGateway: CategoryGateway

    override fun getMocks(): List<Any> {
        return listOf(categoryGateway)
    }

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedId = aCategory.id
        val expectedName = aCategory.name
        val expectedDescription = aCategory.description
        val expectedIsActive = true

        whenever(categoryGateway.findById(expectedId.value)).thenReturn(aCategory.copy())

        val actualCategory = useCase.execute(expectedId.value)

        Assertions.assertEquals(expectedName, aCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(expectedId.value, actualCategory.id)
        Assertions.assertEquals(aCategory.createdAt, actualCategory.createdAt)
        Assertions.assertEquals(aCategory.updatedAt, actualCategory.updatedAt)
        Assertions.assertEquals(aCategory.deletedAt, actualCategory.deletedAt)

    }

    @Test
    fun givenAnInValidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedId = CategoryID.from("132")
        val expectedErrorMessage = "Category with id ${expectedId.value} not found"
        val expectedErrorCount = 1

        whenever(categoryGateway.findById(eq(expectedId.value))).thenReturn(null)

        val domainException = Assertions.assertThrows(DomainException::class.java) { useCase.execute(expectedId.value) }

        Assertions.assertEquals(expectedErrorCount, domainException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, domainException.message)
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldThrowException() {
        val aCategory = Category.newCategory("Film", null, true)
        val expectedId = aCategory.id
        val expectedErrorMessage = "Gateway exception"

        doThrow(IllegalStateException(expectedErrorMessage)).`when`(categoryGateway).findById(eq(expectedId.value))

        val illegalStateException = Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(expectedId.value) }

        verify(categoryGateway, times(1)).findById(eq(expectedId.value))

        Assertions.assertEquals(expectedErrorMessage, illegalStateException.message)
    }
}