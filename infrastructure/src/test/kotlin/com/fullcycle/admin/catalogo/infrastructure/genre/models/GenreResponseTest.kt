package com.fullcycle.admin.catalogo.infrastructure.genre.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
internal class GenreResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<GenreResponse>

    @Test
    fun testMarshall() {
        val expectedId = "123"
        val expectedName = "Film"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()
        val expectedCategories = listOf("123")

        val categoryResponse = GenreResponse(
            id = expectedId,
            name = expectedName,
            active = expectedIsActive,
            categories = expectedCategories,
            createdAt = expectedCreatedAt,
            updatedAt = expectedUpdatedAt,
            deletedAt = expectedDeletedAt
        )

        val jsonContent = json.write(categoryResponse)

        Assertions.assertThat(jsonContent)
            .hasJsonPath("$.id", expectedId)
            .hasJsonPath("$.name", expectedName)
            .hasJsonPath("$.categories_id", expectedCategories)
            .hasJsonPath("$.is_active", expectedIsActive)
            .hasJsonPath("$.created_at", expectedCreatedAt)
            .hasJsonPath("$.updated_at", expectedUpdatedAt)
            .hasJsonPath("$.deleted_at", expectedDeletedAt)
    }

    @Test
    fun testUnmarshall() {
        val expectedId = "123"
        val expectedName = "Film"
        val expectedIsActive = false
        val expectedCategories = listOf("123")
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val json = """
            {"id":"%s",
            "name":"%s",
            "is_active":%s,
            "categories_id":%s,
            "created_at":"%s",
            "updated_at":"%s",
            "deleted_at":"%s"}
        """.trimIndent().format(
            expectedId, expectedName, expectedIsActive, expectedCategories.toString(),
            expectedCreatedAt.toString(), expectedUpdatedAt.toString(), expectedDeletedAt.toString()
        )

        val actualJson = this.json.parse(json)

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("categories", expectedCategories)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
    }
}
