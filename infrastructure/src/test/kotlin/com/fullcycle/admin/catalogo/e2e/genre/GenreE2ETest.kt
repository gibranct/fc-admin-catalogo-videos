package com.fullcycle.admin.catalogo.e2e.genre

import com.fullcycle.admin.catalogo.E2ETest
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.e2e.MockDsl
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@E2ETest
@Testcontainers
internal class GenreE2ETest: MockDsl {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var genreRepository: GenreRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    override fun mvc(): MockMvc {
        return this.mockMvc
    }

    companion object {

        @Container
        private val MYSQL_CONTAINER = MySQLContainer("mysql:8.0")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos")

        @JvmStatic
        @DynamicPropertySource
        private fun setDatasourceProperties(registry: DynamicPropertyRegistry) {
            val mappedPort = MYSQL_CONTAINER.getMappedPort(3306)
            println("Container is running on port $mappedPort")
            registry.add("mysql.port") { mappedPort }
        }

    }

    @Test
    fun `as catalog admin i should be able to create a genre with valid values`() {
        val expectedName = "valid name"
        val expectedCategories = listOf<CategoryID>()
        val expectedIsActive = true

        Assertions.assertEquals(0, genreRepository.count())

        val aGenreId = givenAGenre(expectedName, expectedCategories, expectedIsActive)

        val genreResponse = retrieveGenre(aGenreId)

        Assertions.assertEquals(genreResponse.id, aGenreId.value)
        Assertions.assertEquals(genreResponse.name, expectedName)
        Assertions.assertTrue(
            genreResponse.categories.size == expectedCategories.size
                && genreResponse.categories.containsAll(expectedCategories.map(CategoryID::value))
        )
        Assertions.assertEquals(genreResponse.active, expectedIsActive)
        Assertions.assertNotNull(genreResponse.createdAt)
        Assertions.assertNotNull(genreResponse.updatedAt)
        Assertions.assertNull(genreResponse.deletedAt)
    }

    @Test
    fun `as catalog admin i should be able to update a genre by its identifier with valid values`() {
        val movies = categoryRepository.save(CategoryJpaEntity.from(Category.newCategory("movies", null, true)))
        val series = categoryRepository.save(CategoryJpaEntity.from(Category.newCategory("series", null, true)))
        val expectedName = "valid name"
        val expectedIsActive = true
        val expectedCategories = listOf(movies.id, series.id)

        Assertions.assertEquals(0, genreRepository.count())

        val genreId = givenAGenre("any", listOf(), false)

        val genreResponse = retrieveGenre(genreId)

        Assertions.assertEquals(genreResponse.id, genreId.value)
        Assertions.assertEquals(genreResponse.name, "any")
        Assertions.assertEquals(genreResponse.categories.size, 0)
        Assertions.assertEquals(genreResponse.active, false)

        val updateCategoryRequest = UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive)

        val request = MockMvcRequestBuilders.put("/genres/${genreId.value}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(updateCategoryRequest))

        mockMvc.perform(request)

        val newCategoryResponse = retrieveGenre(genreId)

        Assertions.assertEquals(newCategoryResponse.id, genreId.value)
        Assertions.assertEquals(newCategoryResponse.name, expectedName)
        Assertions.assertTrue(
            newCategoryResponse.categories.size == expectedCategories.size &&
                    newCategoryResponse.categories.containsAll(expectedCategories)
        )
        Assertions.assertEquals(newCategoryResponse.active, expectedIsActive)
    }

    @Test
    fun `as catalog admin i should be able retrieve a genre by its identifier`() {
        val expectedName = "valid name"
        val expectedCategoriesSize = 0
        val expectedIsActive = true

        Assertions.assertEquals(0, genreRepository.count())

        val genreId = givenAGenre(expectedName, listOf(), expectedIsActive)

        val genreResponse = retrieveGenre(genreId)

        Assertions.assertEquals(genreResponse.id, genreId.value)
        Assertions.assertEquals(genreResponse.name, expectedName)
        Assertions.assertTrue(genreResponse.categories.size == expectedCategoriesSize)
        Assertions.assertEquals(genreResponse.active, expectedIsActive)
    }

    @Test
    fun `as catalog admin i should be able delete a genre by its identifier`() {
        val expectedName = "valid name"
        val expectedIsActive = true

        Assertions.assertEquals(0, genreRepository.count())

        val genreId = givenAGenre(expectedName, listOf(), expectedIsActive)

        Assertions.assertEquals(1, genreRepository.count())

        val request = MockMvcRequestBuilders.delete("/genres/${genreId.value}")
            .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)

        Assertions.assertEquals(0, genreRepository.count())
    }

    @Test
    fun `as catalog admin i should be able to navigate to all categories`() {
        Assertions.assertEquals(0, genreRepository.count())

        givenAGenre("Movies", listOf(), true)
        givenAGenre("Documentaries", listOf(), true)
        givenAGenre("TV Shows", listOf(), true)

        listGenres(0, 1, )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize<Int>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Documentaries")))

        listGenres(1, 1, )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize<Int>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Movies")))

        listGenres(2, 1, )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize<Int>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("TV Shows")))

        listGenres(3, 1, )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize<Int>(0)))
    }
}