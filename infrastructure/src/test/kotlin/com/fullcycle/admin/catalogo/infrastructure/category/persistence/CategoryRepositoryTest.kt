package com.fullcycle.admin.catalogo.infrastructure.category.persistence

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.infrastructure.MySQLGatewayTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@MySQLGatewayTest
internal class CategoryRepositoryTest {

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Test
    fun givenAValidaCategory_whenCallsSave_shouldReturnNewCategory() {
        val aCategory = Category.newCategory("film", "new description", true)

        val categoryJpaEntity = CategoryJpaEntity.from(aCategory)

        val newCategory = categoryRepository.saveAndFlush(categoryJpaEntity).toAggregate()

        Assertions.assertThat(aCategory).usingRecursiveComparison().isEqualTo(newCategory)
    }

}