package com.fullcycle.admin.catalogo.infrastructure.genre.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
internal class UpdateGenreRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<UpdateGenreRequest>


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

        val actualJson = this.json.parse(json)

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("categories", expectedCategories)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
    }
}
