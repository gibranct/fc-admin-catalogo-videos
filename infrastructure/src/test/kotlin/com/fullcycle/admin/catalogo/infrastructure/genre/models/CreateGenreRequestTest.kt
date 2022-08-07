package com.fullcycle.admin.catalogo.infrastructure.genre.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
internal class CreateGenreRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<CreateGenreRequest>

    @Test
    fun testMarshall() {
        val expectedName = "Film"
        val expectedCategories = listOf("123")
        val expectedIsActive = false

        val createCategoryRequest = CreateGenreRequest(
            name = expectedName,
            categories = expectedCategories,
            active = expectedIsActive
        )

        val actualJson = this.json.write(createCategoryRequest)

        Assertions.assertThat(actualJson)
            .hasJsonPath("$.name", expectedName)
            .hasJsonPath("$.categories_id", expectedCategories)
            .hasJsonPath("$.is_active", expectedIsActive)
    }


    @Test
    fun testUnmarshall() {
        val expectedName = "Film"
        val expectedCategories = listOf("123")
        val expectedIsActive = false

        val json = """
            {"name":"%s",
            "categories_id":%s,
            "is_active":%s}
        """.trimIndent().format(expectedName, expectedCategories, expectedIsActive)

        val objectContent = this.json.parse(json)

        Assertions.assertThat(objectContent)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("categories", expectedCategories)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
    }
}
