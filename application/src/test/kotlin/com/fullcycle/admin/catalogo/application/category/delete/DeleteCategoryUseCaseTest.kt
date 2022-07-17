package com.fullcycle.admin.catalogo.application.category.delete

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class DeleteCategoryUseCaseTest {

    @InjectMocks
    lateinit var useCase: DefaultDeleteCategoryUseCase

    @Mock
    lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        val aCategory = Category.newCategory("Film", null, true)
        val expectedId = aCategory.id

        doNothing().`when`(categoryGateway).deleteById(eq(expectedId.value))

        useCase.execute(expectedId.value)

        verify(categoryGateway, times(1)).deleteById(eq(expectedId.value))
    }

    @Test
    fun givenAnInValidId_whenCallsDeleteCategory_shouldBeOk() {
        val expectedId = CategoryID.from("123")

        doNothing().`when`(categoryGateway).deleteById(eq(expectedId.value))

        useCase.execute(expectedId.value)

        verify(categoryGateway, times(1)).deleteById(eq(expectedId.value))
    }

    @Test
    fun givenAValidId_whenGatewayThrowException_shouldThrowException() {
        val aCategory = Category.newCategory("Film", null, true)
        val expectedId = aCategory.id

        doThrow(IllegalStateException("Gateway exception")).`when`(categoryGateway).deleteById(eq(expectedId.value))

        Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(expectedId.value) }

        verify(categoryGateway, times(1)).deleteById(eq(expectedId.value))
    }
}