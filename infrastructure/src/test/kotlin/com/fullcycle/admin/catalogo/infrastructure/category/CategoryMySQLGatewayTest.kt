package com.fullcycle.admin.catalogo.infrastructure.category

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategorySeachQuery
import com.fullcycle.admin.catalogo.infrastructure.MySQLGatewayTest
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@MySQLGatewayTest
internal class CategoryMySQLGatewayTest {

    @Autowired
    lateinit var categoryGateway: CategoryGateway

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Test
    fun givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        val expectedName = "Film"
        val expectedDescription = "best film ever"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        assertThat(0).isEqualTo(categoryRepository.count())

        val actualCategory = categoryGateway.create(aCategory)

        assertThat(1).isEqualTo(categoryRepository.count())

        assertThat(actualCategory).usingRecursiveComparison().isEqualTo(actualCategory)
    }

    @Test
    fun givenAValidCategory_whenCallsCreate_shouldReturnAnUpdatedCategory() {
        val expectedName = "Film"
        val expectedDescription = "best film ever"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, null, expectedIsActive)

        assertThat(0).isEqualTo(categoryRepository.count())

        val actualCategory = categoryGateway.create(aCategory)
        assertThat(actualCategory.name).isEqualTo(expectedName)
        assertThat(actualCategory.description).isNull()
        assertThat(actualCategory.isActive).isTrue

        assertThat(1).isEqualTo(categoryRepository.count())

        val updatedCategory = aCategory.copy().update(expectedName, expectedDescription, expectedIsActive)

        assertThat(updatedCategory.name).isEqualTo(expectedName)
        assertThat(updatedCategory.description).isEqualTo(expectedDescription)
        assertThat(updatedCategory.isActive).isTrue
        assertThat(updatedCategory.createdAt).isEqualTo(aCategory.createdAt)
        assertThat(updatedCategory.updatedAt.isAfter(aCategory.updatedAt)).isTrue
        assertThat(updatedCategory.deletedAt).isNull()
    }

    @Test
    fun givenAnPrePersistedCategoryAndAValidCategoryId_whenCallsDeleteByIt_shouldDeleteCategory() {
        val expectedName = "Film"
        val expectedDescription = "best film ever"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        assertThat(0).isEqualTo(categoryRepository.count())

        categoryGateway.create(aCategory)

        assertThat(1).isEqualTo(categoryRepository.count())

        categoryGateway.deleteById(aCategory.id.value)

        assertThat(0).isEqualTo(categoryRepository.count())
    }

    @Test
    fun givenAnPrePersistedCategoryAndAnInValidCategoryId_whenCallsDeleteByIt_shouldNotDeleteCategory() {
        val expectedName = "Film"
        val expectedDescription = "best film ever"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        assertThat(0).isEqualTo(categoryRepository.count())

        categoryGateway.create(aCategory)

        assertThat(1).isEqualTo(categoryRepository.count())

        categoryGateway.deleteById("invalid id")

        assertThat(1).isEqualTo(categoryRepository.count())
    }

    @Test
    fun givenAnPrePersistedCategoryAndAValidCategoryId_whenCallsFindByIt_shouldReturnCategory() {
        val expectedName = "Film"
        val expectedDescription = "best film ever"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        assertThat(0).isEqualTo(categoryRepository.count())

        categoryGateway.create(aCategory)

        assertThat(1).isEqualTo(categoryRepository.count())

        val actualCategory = categoryGateway.findById(aCategory.id.value)

        assertThat(actualCategory).usingRecursiveComparison().isEqualTo(aCategory)
    }

    @Test
    fun givenAnPrePersistedCategoryAndAnInValidCategoryId_whenCallsFindByIt_shouldReturnNull() {
        assertThat(0).isEqualTo(categoryRepository.count())

        val actualCategory = categoryGateway.findById("invalid id")

        assertThat(actualCategory).isNull()
    }


    @Test
    fun givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 3L

        val movie = Category.newCategory("movie", null, true)
        val tvShow = Category.newCategory("tv show", null, true)
        val documentary = Category.newCategory("documentary", null, true)

        assertThat(categoryRepository.count()).isEqualTo(0)

        categoryRepository.saveAll(listOf(
            CategoryJpaEntity.from(movie),
            CategoryJpaEntity.from(tvShow),
            CategoryJpaEntity.from(documentary)
        ))

        assertThat(categoryRepository.count()).isEqualTo(3)

        val query = CategorySeachQuery(0, 1, "", "name", "asc")
        val actualResult = categoryGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.items.size).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
        assertThat(actualResult.items.first().id.value).isEqualTo(documentary.id.value)
    }


    @Test
    fun givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        val expectedPage = 0
        val expectedPerPage = 0
        val expectedTotal = 0L

        assertThat(categoryRepository.count()).isEqualTo(0)

        val query = CategorySeachQuery(0, 1, "doc", "name", "asc")
        val actualResult = categoryGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.items.size).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
    }


    @Test
    fun givenFollowPagination_whenCallsFindAllWithPage_shouldReturnTheRightPage() {
        var expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 3L

        val movie = Category.newCategory("movie", null, true)
        val tvShow = Category.newCategory("tv show", null, true)
        val documentary = Category.newCategory("documentary", null, true)

        assertThat(categoryRepository.count()).isEqualTo(0)

        categoryRepository.saveAll(listOf(
            CategoryJpaEntity.from(movie),
            CategoryJpaEntity.from(tvShow),
            CategoryJpaEntity.from(documentary)
        ))

        assertThat(categoryRepository.count()).isEqualTo(3)

        // Page 0
        var query = CategorySeachQuery(0, 1, "", "name", "asc")
        var actualResult = categoryGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.items.size).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
        assertThat(actualResult.items.first().id.value).isEqualTo(documentary.id.value)

        // Page 1
        expectedPage = 1
        query = CategorySeachQuery(1, 1, "", "name", "asc")
        actualResult = categoryGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.items.size).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
        assertThat(actualResult.items.first().id.value).isEqualTo(movie.id.value)

        // Page 2
        expectedPage = 2
        query = CategorySeachQuery(2, 1, "", "name", "asc")
        actualResult = categoryGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.items.size).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
        assertThat(actualResult.items.first().id.value).isEqualTo(tvShow.id.value)
    }


    @Test
    fun givenPrePersistedCategoriesAndMostAsTerm_whenCallsFindAllAndTermMatchesCategoryDescription_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 1L

        val movie = Category.newCategory("movie", "most viewed category", true)
        val tvShow = Category.newCategory("tv show", "less viewed category", true)
        val documentary = Category.newCategory("documentary", "cool category", true)

        assertThat(categoryRepository.count()).isEqualTo(0)

        categoryRepository.saveAll(listOf(
            CategoryJpaEntity.from(movie),
            CategoryJpaEntity.from(tvShow),
            CategoryJpaEntity.from(documentary)
        ))

        assertThat(categoryRepository.count()).isEqualTo(3)

        val query = CategorySeachQuery(0, 1, "MOST", "name", "asc")
        val actualResult = categoryGateway.findAll(query)

        assertThat(actualResult.currentPage).isEqualTo(expectedPage)
        assertThat(actualResult.items.size).isEqualTo(expectedPerPage)
        assertThat(actualResult.total).isEqualTo(expectedTotal)
        assertThat(actualResult.items.first().id.value).isEqualTo(movie.id.value)
    }
}