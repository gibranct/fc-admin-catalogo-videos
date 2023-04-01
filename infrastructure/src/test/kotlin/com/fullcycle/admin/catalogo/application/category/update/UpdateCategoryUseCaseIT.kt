package com.fullcycle.admin.catalogo.application.category.update

import arrow.core.getOrElse
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.fullcycle.admin.catalogo.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean

@IntegrationTest
internal class UpdateCategoryUseCaseIT {

    @Autowired
    lateinit var updateCategoryUseCase: DefaultUpdateCategoryUseCase

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @SpyBean
    lateinit var categoryGateway: CategoryGateway


    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true
        val expectedId = aCategory.id

        categoryRepository.save(CategoryJpaEntity.from(aCategory))

        Assertions.assertThat(1).isEqualTo(categoryRepository.count())

        val aCommand = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        val actualOutput = updateCategoryUseCase.execute(aCommand).getOrElse { return  }

        Assertions.assertThat(actualOutput).isNotNull
        Assertions.assertThat(actualOutput.id).isNotNull

        val actualCategory = categoryRepository.findById(expectedId.value).get().toAggregate()

        Assertions.assertThat(1).isEqualTo(categoryRepository.count())

        Assertions.assertThat(actualCategory.id.value).isEqualTo(aCategory.id.value)
        Assertions.assertThat(actualCategory.name).isEqualTo(expectedName)
        Assertions.assertThat(actualCategory.description).isEqualTo(expectedDescription)
        Assertions.assertThat(actualCategory.isActive).isEqualTo(expectedIsActive)
        Assertions.assertThat(actualCategory.createdAt).isNotNull
        Assertions.assertThat(actualCategory.updatedAt.isAfter(aCategory.updatedAt)).isTrue
        Assertions.assertThat(actualCategory.deletedAt).isNull()
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = false
        val expectedId = aCategory.id

        categoryRepository.save(CategoryJpaEntity.from(aCategory))

        Assertions.assertThat(1).isEqualTo(categoryRepository.count())

        val aCommand = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        val actualOutput = updateCategoryUseCase.execute(aCommand).getOrElse { return  }

        Assertions.assertThat(actualOutput).isNotNull
        Assertions.assertThat(actualOutput.id).isNotNull

        val actualCategory = categoryRepository.findById(expectedId.value).get().toAggregate()

        Assertions.assertThat(1).isEqualTo(categoryRepository.count())

        Assertions.assertThat(actualCategory.id.value).isEqualTo(aCategory.id.value)
        Assertions.assertThat(actualCategory.name).isEqualTo(expectedName)
        Assertions.assertThat(actualCategory.description).isEqualTo(expectedDescription)
        Assertions.assertThat(actualCategory.isActive).isEqualTo(expectedIsActive)
        Assertions.assertThat(actualCategory.createdAt).isNotNull
        Assertions.assertThat(actualCategory.updatedAt.isAfter(aCategory.updatedAt)).isTrue
        Assertions.assertThat(actualCategory.deletedAt).isNotNull
    }

    @Test
    fun givenAValidCommand_whenGatewayThrows_shouldReturnAnException() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedId = aCategory.id
        val expectedErrorMessage = "error"
        val expectedErrorCount = 1

        categoryRepository.save(CategoryJpaEntity.from(aCategory))

        val aCommand = UpdateCategoryCommand.with(expectedId.value, "valid name", "new description", true)

        doThrow(IllegalStateException(expectedErrorMessage)).`when`(categoryGateway).update(any())

        val notification = updateCategoryUseCase.execute(aCommand).fold({ it }, { Notification.create() })

        Assertions.assertThat(notification.firstError()?.message).isEqualTo(expectedErrorMessage)
        Assertions.assertThat((notification.getErrors().size)).isEqualTo(expectedErrorCount)

        verify(categoryGateway, Mockito.times(1)).findById(eq(aCategory.id.value))
    }
}