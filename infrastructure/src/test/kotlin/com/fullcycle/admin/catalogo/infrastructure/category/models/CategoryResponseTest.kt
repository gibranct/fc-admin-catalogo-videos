package com.fullcycle.admin.catalogo.infrastructure.category.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
internal class CategoryResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<CategoryResponse>

    @Test
    fun testMarshall() {
        val expectedId = "123"
        val expectedName = "Film"
        val expectedDescription = "description"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val categoryResponse = CategoryResponse(
            id = expectedId,
            name = expectedName,
            description = expectedDescription,
            isActive = expectedIsActive,
            createdAt = expectedCreatedAt,
            updatedAt = expectedUpdatedAt,
            deletedAt = expectedDeletedAt
        )

        val jsonContent = json.write(categoryResponse)

        Assertions.assertThat(jsonContent)
            .hasJsonPath("$.id", expectedId)
            .hasJsonPath("$.name", expectedName)
            .hasJsonPath("$.description", expectedDescription)
            .hasJsonPath("$.is_active", expectedIsActive)
            .hasJsonPath("$.created_at", expectedCreatedAt)
            .hasJsonPath("$.updated_at", expectedUpdatedAt)
            .hasJsonPath("$.deleted_at", expectedDeletedAt)
    }

    @Test
    fun testUnmarshall() {
        val expectedId = "123"
        val expectedName = "Film"
        val expectedDescription = "description"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val json = """
            {"id":"%s",
            "name":"%s",
            "description":"%s",
            "is_active":%s,
            "created_at":"%s",
            "updated_at":"%s",
            "deleted_at":"%s"}
        """.trimIndent().format(
            expectedId, expectedName, expectedDescription, expectedIsActive,
            expectedCreatedAt.toString(), expectedUpdatedAt.toString(), expectedDeletedAt.toString()
        )

        val actualJson = this.json.parse(json)

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("isActive", expectedIsActive)
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
    }
}
