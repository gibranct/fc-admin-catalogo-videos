package com.fullcycle.admin.catalogo.infrastructure.genre.models

import com.fullcycle.admin.catalogo.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JacksonTest
internal class GenreListResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<GenreListResponse>

    @Test
    fun testMarshall() {
        val expectedId = "123"
        val expectedName = "Film"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()

        val categoryListResponse = GenreListResponse(
            id = expectedId,
            name = expectedName,
            active = expectedIsActive,
            createdAt = expectedCreatedAt,
        )

        val jsonContent = json.write(categoryListResponse)

        Assertions.assertThat(jsonContent)
            .hasJsonPath("$.id", expectedId)
            .hasJsonPath("$.name", expectedName)
            .hasJsonPath("$.is_active", expectedIsActive)
            .hasJsonPath("$.created_at", expectedCreatedAt)
    }

}