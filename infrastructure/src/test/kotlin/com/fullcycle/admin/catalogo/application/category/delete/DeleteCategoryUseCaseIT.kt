package com.fullcycle.admin.catalogo.application.category.delete

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.IntegrationTest
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean

@IntegrationTest
internal class DeleteCategoryUseCaseIT {

    @Autowired
    lateinit var deleteCategoryUseCase: DefaultDeleteCategoryUseCase

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @SpyBean
    lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        val aCategory = Category.newCategory("Film", null, true)
        val expectedId = aCategory.id

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.save(CategoryJpaEntity.from(aCategory))

        Assertions.assertEquals(1, categoryRepository.count())

        deleteCategoryUseCase.execute(expectedId.value)

        Assertions.assertEquals(0, categoryRepository.count())

        verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId.value))
    }

    @Test
    fun givenAnInValidId_whenCallsDeleteCategory_shouldBeOk() {
        val expectedId = CategoryID.from("123")

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.save(CategoryJpaEntity.from(Category.newCategory("Film", null, true)))

        Assertions.assertEquals(1, categoryRepository.count())

        deleteCategoryUseCase.execute(expectedId.value)

        Assertions.assertEquals(1, categoryRepository.count())

        verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId.value))
    }

    @Test
    fun givenAValidId_whenGatewayThrowException_shouldThrowException() {
        val aCategory = Category.newCategory("Film", null, true)
        val expectedId = aCategory.id

        doThrow(IllegalStateException("Gateway exception")).`when`(categoryGateway).deleteById(eq(expectedId.value))

        Assertions.assertThrows(IllegalStateException::class.java) { deleteCategoryUseCase.execute(expectedId.value) }

        verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId.value))
    }

}