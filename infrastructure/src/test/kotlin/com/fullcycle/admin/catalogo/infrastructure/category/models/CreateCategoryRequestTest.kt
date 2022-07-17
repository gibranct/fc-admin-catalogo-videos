package com.fullcycle.admin.catalogo.infrastructure.category.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
internal class CreateCategoryRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<CreateCategoryRequest>

    @Test
    fun testMarshall() {
        val expectedName = "Film"
        val expectedDescription = "description"
        val expectedIsActive = false

        val createCategoryRequest = CreateCategoryRequest(
            name = expectedName,
            description = expectedDescription,
            isActive = expectedIsActive
        )

        val actualJson = this.json.write(createCategoryRequest)

        Assertions.assertThat(actualJson)
            .hasJsonPath("$.name", expectedName)
            .hasJsonPath("$.description", expectedDescription)
            .hasJsonPath("$.is_active", expectedIsActive)
    }


    @Test
    fun testUnmarshall() {
        val expectedName = "Film"
        val expectedDescription = "description"
        val expectedIsActive = false

        val json = """
            {"name":"%s",
            "description":"%s",
            "is_active":%s}
        """.trimIndent().format(expectedName, expectedDescription, expectedIsActive)

        val objectContent = this.json.parse(json)

        Assertions.assertThat(objectContent)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("isActive", expectedIsActive)
    }
}
