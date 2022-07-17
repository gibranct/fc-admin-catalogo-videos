package com.fullcycle.admin.catalogo.application.category.create

import arrow.core.getOrElse
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.fullcycle.admin.catalogo.infrastructure.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean

@IntegrationTest
internal class CreateCategoryUseCaseIT {

    @Autowired
    lateinit var createCategoryUseCase: DefaultCreateCategoryUseCase

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @SpyBean
    lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        Assertions.assertThat(0).isEqualTo(categoryRepository.count())

        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        val actualOutput = createCategoryUseCase.execute(aCommand).getOrElse { return  }

        Assertions.assertThat(1).isEqualTo(categoryRepository.count())

        val newCategory = categoryRepository.findById(actualOutput.id).get()

        Assertions.assertThat(newCategory.id).isEqualTo(newCategory.id)
        Assertions.assertThat(newCategory.name).isEqualTo(expectedName)
        Assertions.assertThat(newCategory.description).isEqualTo(expectedDescription)
        Assertions.assertThat(newCategory.isActive).isEqualTo(expectedIsActive)
        Assertions.assertThat(newCategory.createdAt).isNotNull
        Assertions.assertThat(newCategory.updatedAt).isNotNull
        Assertions.assertThat(newCategory.deletedAt).isNull()
    }

    @Test
    fun givenAValidCommand_whenGatewayThrows_shouldReturnAnException() {
        val expectedErrorMessage = "Gateway exception"
        val expectedErrorCount = 1

        val aCommand = CreateCategoryCommand.with("valid name", "new description", true)

        doThrow(IllegalArgumentException("Gateway exception")).`when`(categoryGateway).create(any())

        val notification = createCategoryUseCase.execute(aCommand).fold({ it }, { Notification.create() })

        Assertions.assertThat(notification.firstError()?.message).isEqualTo(expectedErrorMessage)
        Assertions.assertThat(notification.getErrors().size).isEqualTo(expectedErrorCount)

        verify(categoryGateway,  Mockito.times(1)).create(any())
    }
}