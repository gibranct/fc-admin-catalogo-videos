package com.fullcycle.admin.catalogo.infrastructure.category.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
internal class CategoryListResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<CategoryListResponse>

    @Test
    fun testMarshall() {
        val expectedId = "123"
        val expectedName = "Film"
        val expectedDescription = "description"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val categoryListResponse = CategoryListResponse(
            id = expectedId,
            name = expectedName,
            description = expectedDescription,
            isActive = expectedIsActive,
            createdAt = expectedCreatedAt,
            deletedAt = expectedDeletedAt
        )

        val jsonContent = json.write(categoryListResponse)

        Assertions.assertThat(jsonContent)
            .hasJsonPath("$.id", expectedId)
            .hasJsonPath("$.name", expectedName)
            .hasJsonPath("$.description", expectedDescription)
            .hasJsonPath("$.is_active", expectedIsActive)
            .hasJsonPath("$.created_at", expectedCreatedAt)
            .hasJsonPath("$.deleted_at", expectedDeletedAt)
    }

}