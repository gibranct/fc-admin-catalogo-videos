package com.fullcycle.admin.catalogo.application.category.retrieve.get

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.infrastructure.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean

@IntegrationTest
internal class GetCategoryByIdUseCaseIT {

    @Autowired
    lateinit var getCategoryByIdUseCase: DefaultGetCategoryByIdUseCase

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @SpyBean
    lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val aCategory = Category.newCategory("Film", null, true)

        val expectedId = aCategory.id

        categoryRepository.save(CategoryJpaEntity.from(aCategory))

        val actualCategory = getCategoryByIdUseCase.execute(expectedId.value)

        assertThat(aCategory.id.value).isEqualTo(actualCategory.id)
        assertThat(aCategory.name).isEqualTo(actualCategory.name)
        assertThat(aCategory.description).isEqualTo(actualCategory.description)
        assertThat(aCategory.isActive).isEqualTo(actualCategory.isActive)
        assertThat(aCategory.createdAt).isEqualTo(actualCategory.createdAt)
        assertThat(aCategory.updatedAt).isEqualTo(actualCategory.updatedAt)
        assertThat(actualCategory.deletedAt).isNull()
    }

    @Test
    fun givenAnInValidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedId = CategoryID.from("132")
        val expectedErrorMessage = "Category with id ${expectedId.value} not found"
        val expectedErrorCount = 1

        val notFoundException = Assertions.assertThrows(NotFoundException::class.java) { getCategoryByIdUseCase.execute(expectedId.value) }

        Assertions.assertEquals(expectedErrorCount, notFoundException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, notFoundException.message)
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldThrowException() {
        val aCategory = Category.newCategory("Film", null, true)
        val expectedId = aCategory.id
        val expectedErrorMessage = "Gateway exception"

        doThrow(IllegalStateException(expectedErrorMessage)).`when`(categoryGateway).findById(eq(expectedId.value))

        val illegalStateException = Assertions.assertThrows(IllegalStateException::class.java) { getCategoryByIdUseCase.execute(expectedId.value) }

        verify(categoryGateway, Mockito.times(1)).findById(eq(expectedId.value))

        Assertions.assertEquals(expectedErrorMessage, illegalStateException.message)
    }
}