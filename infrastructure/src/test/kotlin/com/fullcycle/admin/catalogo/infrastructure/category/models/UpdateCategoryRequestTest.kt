package com.fullcycle.admin.catalogo.infrastructure.category.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
internal class UpdateCategoryRequestTest {

    @Autowired
    private lateinit var json: JacksonTester<UpdateCategoryRequest>


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

        val actualJson = this.json.parse(json)

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("isActive", expectedIsActive)
    }
}
