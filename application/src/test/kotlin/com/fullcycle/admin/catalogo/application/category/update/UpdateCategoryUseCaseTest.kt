package com.fullcycle.admin.catalogo.application.category.update

import arrow.core.getOrElse
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UpdateCategoryUseCaseTest {

    @InjectMocks
    lateinit var useCase: DefaultUpdateCategoryUseCase

    @Mock
    lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true
        val expectedId = aCategory.id

        val aCommand = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        whenever(categoryGateway.findById(eq(expectedId.value))).thenReturn(aCategory.copy())

        whenever(categoryGateway.update(any())).thenAnswer {
            it.arguments[0]
        }

        val actualOutput = useCase.execute(aCommand).getOrElse { return  }

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        verify(categoryGateway, times(1)).findById(eq(aCategory.id.value))

        verify(categoryGateway, times(1))
            .update(argThat { updatedCategory ->
                Objects.equals(expectedName, updatedCategory.name)
                        && Objects.equals(expectedDescription, updatedCategory.description)
                        && Objects.equals(expectedIsActive, updatedCategory.isActive)
                        && Objects.equals(expectedId, updatedCategory.id)
                        && Objects.equals(aCategory.createdAt, updatedCategory.createdAt)
                        && aCategory.updatedAt.isBefore(updatedCategory.updatedAt)
                        && Objects.isNull(updatedCategory.deletedAt)
            })
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = false
        val expectedId = aCategory.id

        val aCommand = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        whenever(categoryGateway.findById(eq(expectedId.value))).thenReturn(aCategory.copy())

        whenever(categoryGateway.update(any())).thenAnswer {
            it.arguments[0]
        }

        Assertions.assertTrue(aCategory.isActive)

        val actualOutput = useCase.execute(aCommand).getOrElse { return  }

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        verify(categoryGateway, times(1)).findById(eq(aCategory.id.value))

        verify(categoryGateway, times(1))
            .update(argThat { updatedCategory ->
                Objects.equals(expectedName, updatedCategory.name)
                        && Objects.equals(expectedDescription, updatedCategory.description)
                        && Objects.equals(expectedIsActive, updatedCategory.isActive)
                        && Objects.equals(expectedId, updatedCategory.id)
                        && Objects.equals(aCategory.createdAt, updatedCategory.createdAt)
                        && aCategory.updatedAt.isBefore(updatedCategory.updatedAt)
                        && Objects.nonNull(updatedCategory.deletedAt)
            })
    }

    @Test
    fun givenAValidCommand_whenGatewayThrows_shouldReturnAnException() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedErrorMessage = "Gateway exception"
        val expectedErrorCount = 1

        val aCommand = UpdateCategoryCommand.with(aCategory.id.value, "valid name", "new description", true)

        whenever(categoryGateway.findById(eq(aCategory.id.value))).thenReturn(aCategory.copy())

        whenever(categoryGateway.update(any())).thenThrow(IllegalArgumentException("Gateway exception"))

        val notification = useCase.execute(aCommand).fold({ it }, { Notification.create() })

        Assertions.assertEquals(notification.firstError()?.message, expectedErrorMessage)
        Assertions.assertEquals(notification.getErrors().size, expectedErrorCount)

        verify(categoryGateway, times(1)).findById(eq(aCategory.id.value))

        verify(categoryGateway, times(1)).update(any())
    }

    @Test
    fun givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnAnNotFoundException() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedErrorMessage = "Category with id ${aCategory.id.value} not found"
        val expectedErrorCount = 1

        val aCommand = UpdateCategoryCommand.with(aCategory.id.value, "valid name", "new description", true)

        whenever(categoryGateway.findById(eq(aCategory.id.value))).thenReturn(null)

        val domainException = Assertions.assertThrows(DomainException::class.java) { useCase.execute(aCommand) }

        Assertions.assertEquals(domainException.message, expectedErrorMessage)
        Assertions.assertEquals(domainException.errors.size, expectedErrorCount)

        verify(categoryGateway, times(1)).findById(eq(aCategory.id.value))

        verify(categoryGateway, times(0)).update(any())
    }

    @ParameterizedTest
    @MethodSource("invalidNamesAndMessages")
    fun givenAnInvalidName_whenCallsUpdateCategory_shouldReturnADomainException(expectedName: String, expectedErrorMessage: String, expectedErrorCount: Int) {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedDescription = "new description"
        val expectedIsActive = true
        val expectedId = aCategory.id

        whenever(categoryGateway.findById(eq(expectedId.value))).thenReturn(aCategory.copy())

        val aCommand = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        val notification = useCase.execute(aCommand).fold({ it }, { Notification.create() })

        Assertions.assertEquals(notification.getErrors().size, expectedErrorCount)
        Assertions.assertEquals(notification.firstError()?.message, expectedErrorMessage)
        verify(categoryGateway, times(0)).update(any())
    }

    companion object {
        private val STRING_GREATER_THAN_255 = "A".padEnd(256, 'B')

        @JvmStatic
        fun invalidNamesAndMessages() = listOf(
            Arguments.of("", "'name' should not be null or empty", 2),
            Arguments.of("fa", "'name' must be between 3 and 255 characters", 1),
            Arguments.of(STRING_GREATER_THAN_255, "'name' must be between 3 and 255 characters", 1)
        )
    }
}