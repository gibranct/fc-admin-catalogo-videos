package com.fullcycle.admin.catalogo.e2e.category

import com.fullcycle.admin.catalogo.e2e.MockDsl
import com.fullcycle.admin.catalogo.infrastructure.E2ETest
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@E2ETest
@Testcontainers
internal class CategoryE2ETest: MockDsl {

    @Autowired
    lateinit var mockMvc: MockMvc

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
    fun `as catalog admin i should be able to create a category with valid values`() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        Assertions.assertThat(0).isEqualTo(categoryRepository.count())

        val aCategoryId = givenACategory(expectedName, expectedDescription, expectedIsActive)

        val categoryResponse = retrieveCategory(aCategoryId)

        Assertions.assertThat(categoryResponse.id).isEqualTo(aCategoryId.value)
        Assertions.assertThat(categoryResponse.name).isEqualTo(expectedName)
        Assertions.assertThat(categoryResponse.description).isEqualTo(expectedDescription)
        Assertions.assertThat(categoryResponse.isActive).isEqualTo(expectedIsActive)
    }

    @Test
    fun `as catalog admin i should be able to update a category by its identifier with valid values`() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        Assertions.assertThat(0).isEqualTo(categoryRepository.count())

        val aCategoryId = givenACategory("any", null, false)

        val categoryResponse = retrieveCategory(aCategoryId)

        Assertions.assertThat(categoryResponse.id).isEqualTo(aCategoryId.value)
        Assertions.assertThat(categoryResponse.name).isEqualTo("any")
        Assertions.assertThat(categoryResponse.description).isNull()
        Assertions.assertThat(categoryResponse.isActive).isEqualTo(false)

        val updateCategoryRequest = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        val request = put("/categories/${aCategoryId.value}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(updateCategoryRequest))

        mockMvc.perform(request)

        val newCategoryResponse = retrieveCategory(aCategoryId)

        Assertions.assertThat(newCategoryResponse.id).isEqualTo(aCategoryId.value)
        Assertions.assertThat(newCategoryResponse.name).isEqualTo(expectedName)
        Assertions.assertThat(newCategoryResponse.description).isEqualTo(expectedDescription)
        Assertions.assertThat(newCategoryResponse.isActive).isEqualTo(expectedIsActive)
    }

    @Test
    fun `as catalog admin i should be able retrieve a category by its identifier`() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        Assertions.assertThat(0).isEqualTo(categoryRepository.count())

        val aCategoryId = givenACategory(expectedName, expectedDescription, expectedIsActive)

        val categoryResponse = retrieveCategory(aCategoryId)

        Assertions.assertThat(categoryResponse.id).isEqualTo(aCategoryId.value)
        Assertions.assertThat(categoryResponse.name).isEqualTo(expectedName)
        Assertions.assertThat(categoryResponse.description).isEqualTo(expectedDescription)
        Assertions.assertThat(categoryResponse.isActive).isEqualTo(expectedIsActive)
    }

    @Test
    fun `as catalog admin i should be able delete a category by its identifier`() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        Assertions.assertThat(0).isEqualTo(categoryRepository.count())

        val aCategoryId = givenACategory(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertThat(1).isEqualTo(categoryRepository.count())

        val request = delete("/categories/${aCategoryId.value}")
            .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)

        Assertions.assertThat(0).isEqualTo(categoryRepository.count())
    }

    @Test
    fun `as catalog admin i should be able to navigate to all categories`() {
        Assertions.assertThat(0).isEqualTo(categoryRepository.count())

        givenACategory("Movies", null, true)
        givenACategory("Documentaries", null, true)
        givenACategory("TV Shows", null, true)

        listCategories(0, 1, )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
            .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(jsonPath("$.items", Matchers.hasSize<Int>(1)))
            .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Documentaries")))

        listCategories(1, 1, )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.current_page", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(jsonPath("$.items", Matchers.hasSize<Int>(1)))
            .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Movies")))

        listCategories(2, 1, )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.current_page", Matchers.equalTo(2)))
            .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(jsonPath("$.items", Matchers.hasSize<Int>(1)))
            .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("TV Shows")))

        listCategories(3, 1, )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.current_page", Matchers.equalTo(3)))
            .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(jsonPath("$.items", Matchers.hasSize<Int>(0)))
    }
}