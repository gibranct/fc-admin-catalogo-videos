package com.fullcycle.admin.catalogo.e2e

import com.fullcycle.admin.catalogo.domain.Identifier
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.GenreId
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

interface MockDsl {

    fun mvc(): MockMvc

    fun givenACategory(expectedName: String, expectedDescription: String?, expectedIsActive: Boolean): CategoryID {
        val categoryRequest = CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        val categoryId = this.given("/categories", categoryRequest)

        return CategoryID.from(categoryId!!)
    }

    fun givenAGenre(expectedName: String, categories: List<CategoryID>, expectedIsActive: Boolean): GenreId {
        val genreRequest = CreateGenreRequest(expectedName, categories.map(CategoryID::value), expectedIsActive)

        val genreId = this.given("/genres", genreRequest)

        return GenreId.from(genreId!!)
    }

    fun retrieveGenre(anId: GenreId): GenreResponse {
        return this.retrieve("/genres/", anId, GenreResponse::class.java)
    }

    fun retrieveCategory(anId: CategoryID): CategoryResponse {
        return this.retrieve("/categories/", anId, CategoryResponse::class.java)
    }

    fun listCategories(page: Int, perPage: Int, sort: String, dir: String, search: String): ResultActions {
        return this.list("/categories", page, perPage, sort, dir, search)
    }

    fun listGenres(page: Int, perPage: Int, sort: String, dir: String, search: String): ResultActions {
        return this.list("/genres", page, perPage, sort, dir, search)
    }

    fun listGenres(page: Int, perPage: Int): ResultActions {
        return listGenres(page, perPage, "", "", "")
    }

    fun listCategories(page: Int, perPage: Int): ResultActions {
        return listCategories(page, perPage, "", "", "")
    }

    private fun given(url: String, body: Any): String? {

        val request = MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(body))

        return this.mvc().perform(request)
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()
            .response.getHeader("Location")
            ?.replace("${url}/", "")
    }

    private fun <T>retrieve(url: String, anId: Identifier, clazz: Class<T>): T {
        val request = MockMvcRequestBuilders.get(url + anId.value)

        val json = this.mvc().perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
            .response.contentAsString

        return Json.readValue(json, clazz)
    }

    private fun list(
        url: String,
        page: Int,
        perPage: Int,
        sort: String,
        dir: String,
        search: String
    ): ResultActions {
        val request = MockMvcRequestBuilders.get(url)
            .queryParam("page", page.toString())
            .queryParam("perPage", perPage.toString())
            .queryParam("sort", sort)
            .queryParam("dir", dir)
            .queryParam("search", search)

        return this.mvc().perform(request)
    }
}