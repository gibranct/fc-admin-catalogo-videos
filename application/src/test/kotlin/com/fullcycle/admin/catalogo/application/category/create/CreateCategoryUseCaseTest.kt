package com.fullcycle.admin.catalogo.application.category.create

import arrow.core.getOrElse
import com.fullcycle.admin.catalogo.application.UseCaseTest
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import java.util.*

class CreateCategoryUseCaseTest: UseCaseTest() {

    @InjectMocks
    lateinit var useCase: DefaultCreateCategoryUseCase

    @Mock
    lateinit var categoryGateway: CategoryGateway

    override fun getMocks(): List<Any> {
        return listOf(categoryGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        whenever(categoryGateway.create(any())).thenAnswer {
            it.arguments[0]
        }

        val actualOutput = useCase.execute(aCommand).getOrElse { return  }

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        verify(categoryGateway, times(1))
            .create(argThat { aCategory ->
                Objects.equals(expectedName, aCategory.name)
                        && Objects.equals(expectedDescription, aCategory.description)
                        && Objects.equals(expectedIsActive, aCategory.isActive)
                        && Objects.nonNull(aCategory.id)
                        && Objects.nonNull(aCategory.createdAt)
                        && Objects.nonNull(aCategory.updatedAt)
                        && Objects.isNull(aCategory.deletedAt)
            })
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = false

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        whenever(categoryGateway.create(any())).thenAnswer {
            it.arguments[0]
        }

        val actualOutput = useCase.execute(aCommand).getOrElse { return }

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        verify(categoryGateway, times(1))
            .create(argThat { aCategory ->
                Objects.equals(expectedName, aCategory.name)
                        && Objects.equals(expectedDescription, aCategory.description)
                        && Objects.equals(expectedIsActive, aCategory.isActive)
                        && Objects.nonNull(aCategory.id)
                        && Objects.nonNull(aCategory.createdAt)
                        && Objects.nonNull(aCategory.updatedAt)
                        && Objects.nonNull(aCategory.deletedAt)
            })
    }

    @Test
    fun givenAValidCommand_whenGatewayThrows_shouldReturnAnException() {
        val expectedErrorMessage = "Gateway exception"
        val expectedErrorCount = 1

        val aCommand = CreateCategoryCommand.with("valid name", "new description", true)

        whenever(categoryGateway.create(any())).thenThrow(IllegalArgumentException("Gateway exception"))

        val notification = useCase.execute(aCommand).fold({ it }, { Notification.create() })

        Assertions.assertEquals(notification.firstError()?.message, expectedErrorMessage)
        Assertions.assertEquals(notification.getErrors().size, expectedErrorCount)

        verify(categoryGateway, times(1)).create(any())
    }

    @ParameterizedTest
    @MethodSource("invalidNamesAndMessages")
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnADomainException(expectedName: String, expectedErrorMessage: String, expectedErrorCount: Int) {
        val expectedDescription = "new description"
        val expectedIsActive = true

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        val notification = useCase.execute(aCommand).fold({ it }, { Notification.create() })

        Assertions.assertEquals(notification.getErrors().size, expectedErrorCount)
        Assertions.assertEquals(notification.firstError()?.message, expectedErrorMessage)
        verify(categoryGateway, times(0)).create(any())
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