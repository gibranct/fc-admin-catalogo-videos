package com.fullcycle.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fullcycle.admin.catalogo.ControllerTest
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreOutput
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenresOutput
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenresUseCase
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreOutput
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreId
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Objects

@ControllerTest(controllers = [GenreAPI::class])
class GenreAPITest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var createGenreUseCase: CreateGenreUseCase

    @MockBean
    private lateinit var getGenreByIdUseCase: GetGenreByIdUseCase

    @MockBean
    private lateinit var updateGenreUseCase: UpdateGenreUseCase

    @MockBean
    private lateinit var deleteGenreUseCase: DeleteGenreUseCase

    @MockBean
    private lateinit var listGenresUseCase: ListGenresUseCase

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "valid name"
        val expectedCategoriesIds = listOf("123", "456")
        val expectedIsActive = true
        val expectedGenreId = "789"

        val anInput = CreateGenreRequest(expectedName, expectedCategoriesIds, expectedIsActive)

        Mockito.`when`(createGenreUseCase.execute(any()))
            .thenReturn(CreateGenreOutput.from(expectedGenreId))

        val request = post("/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isCreated,
                header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", Matchers.equalTo(expectedGenreId))
            )

        verify(createGenreUseCase, times(1)).execute(argThat { command ->
            Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedCategoriesIds, command.categoriesIds)
                    && Objects.equals(expectedIsActive, command.isActive)
        })
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnNotification() {
        val expectedName = ""
        val expectedCategoriesIds = listOf("123", "456")
        val expectedIsActive = true
        val expectedMessage = "'name' should not be null or empty"

        val anInput = CreateGenreRequest(expectedName, expectedCategoriesIds, expectedIsActive)

        Mockito.`when`(createGenreUseCase.execute(any())).thenThrow(NotificationException("Error", Notification.create(Error(expectedMessage))))

        val request = post("/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isUnprocessableEntity,
                header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.errors", Matchers.hasSize<Int>(1)),
                jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
            )

        verify(createGenreUseCase, times(1)).execute(argThat { command ->
            Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedCategoriesIds, command.categoriesIds)
                    && Objects.equals(expectedIsActive, command.isActive)
        })
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnADomainException() {
        val expectedName = ""
        val expectedDescription = "new description"
        val expectedIsActive = true
        val expectedMessage = "'name' should not be null or empty"
        val expectedCategoriesIds = listOf<String>()

        val anInput = CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        Mockito.`when`(createGenreUseCase.execute(any()))
            .thenThrow(DomainException.with(Error(expectedMessage)))

        val request = post("/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isUnprocessableEntity,
                header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.errors", Matchers.hasSize<Int>(1)),
                jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage)),
                jsonPath("$.message", Matchers.equalTo(expectedMessage))
            )

        verify(createGenreUseCase, times(1)).execute(argThat { command ->
            Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedCategoriesIds, command.categoriesIds)
                    && Objects.equals(expectedIsActive, command.isActive)
        })
    }

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val expectedName = "valid name"
        val expectedCategoriesIds = listOf<String>()
        val expectedIsActive = true

        val aGenre = Genre.newGenre(expectedName, expectedIsActive)

        val expectedId = aGenre.id.value

        Mockito.`when`(getGenreByIdUseCase.execute(any()))
            .thenReturn(GenreOutput.from(aGenre))

        val request = get("/genres/${expectedId}")
            .accept(MediaType.APPLICATION_JSON_VALUE)

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isOk,
                header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", Matchers.equalTo(expectedId)),
                jsonPath("$.name", Matchers.equalTo(expectedName)),
                jsonPath("$.categories_id", Matchers.equalTo(expectedCategoriesIds)),
                jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)),
            )

        verify(getGenreByIdUseCase, times(1)).execute(argThat { input ->
            Objects.equals(input, expectedId)
        })
    }

    @Test
    fun givenAnInValidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedId = GenreId.from("123")
        val expectedErrorMessage = "${Genre::class.simpleName} with id ${expectedId.value} not found"

        Mockito.`when`(getGenreByIdUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Genre::class, expectedId))

        val request = get("/genres/${expectedId.value}")

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isNotFound,
                header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.message", Matchers.equalTo(expectedErrorMessage))
            )
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val aGenre = Genre.newGenre("Film",true)

        val expectedName = "valid name"
        val expectedCategoriesIds = listOf<String>()
        val expectedIsActive = true
        val expectedId = aGenre.id

        Mockito.`when`(updateGenreUseCase.execute(any())).thenReturn(UpdateGenreOutput.from(aGenre))

        val anInput = UpdateGenreRequest (expectedName, expectedCategoriesIds, expectedIsActive)

        val request = put("/genres/${expectedId.value}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.value)))

        verify(updateGenreUseCase, times(1)).execute(argThat { command ->
            Objects.equals(command.id, expectedId.value)
                    && Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedCategoriesIds, command.categoriesIds)
                    && Objects.equals(expectedIsActive, command.isActive)
        })

    }

    @Test
    fun givenAnInvalidId_whenCallsUpdateCategory_shouldReturnNotFound() {
        val expectedId = "9999"
        val expectedErrorMessage = "Genre with id $expectedId not found"
        val expectedCategoriesIds = listOf<String>("123", "456")

        Mockito.`when`(updateGenreUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Genre::class, GenreId.from(expectedId)))

        val anInput = UpdateGenreRequest("any", expectedCategoriesIds, true)

        val request = put("/genres/${expectedId}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateCategory_shouldReturnNotification() {
        val expectedId = "9999"
        val expectedErrorMessage = "'name' should not be null or empty"
        val expectedErrorCount = 1
        val expectedCategoriesIds = listOf<String>()

        Mockito.`when`(updateGenreUseCase.execute(any())).thenThrow(NotificationException("", Notification.create(Error(expectedErrorMessage))))

        val anInput = UpdateGenreRequest("any", expectedCategoriesIds, true)

        val request = put("/genres/${expectedId}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)))
            .andExpect(jsonPath("$.errors", Matchers.hasSize<Int>(expectedErrorCount)))
    }

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        val expectedId = "123"

        doNothing().`when`(deleteGenreUseCase).execute(any())

        val request = delete("/genres/${expectedId}")
            .accept(MediaType.APPLICATION_JSON_VALUE)

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNoContent)

        verify(deleteGenreUseCase, times(1)).execute(argThat { input ->
            Objects.equals(input, expectedId)
        })
    }

    @Test
    fun givenValidParams_whenCallsListCategories_shouldReturnCategories() {
        val genre = Genre.newGenre("Film", true)

        val expectedPage = 0
        val expectedCategoriesIds = listOf(CategoryID.from("123"), CategoryID.from("456"))
        val expectedPerPage = 10
        val expectedItemsCount = 1
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTerms = "movies"
        val expectedTotal: Long = 1
        val expectedItems = listOf(ListGenresOutput.from(genre))

        genre.addCategoriesIds(expectedCategoriesIds)

        Mockito.`when`(listGenresUseCase.execute(any()))
            .thenReturn(Pagination(
                currentPage = expectedPage,
                perPage = expectedPerPage,
                total = expectedTotal,
                items = expectedItems
            ))

        val request = get("/genres")
            .queryParam("page", expectedPage.toString())
            .queryParam("perPage", expectedPerPage.toString())
            .queryParam("sort", expectedSort)
            .queryParam("dir", expectedDirection)
            .queryParam("search", expectedTerms)
            .accept(MediaType.APPLICATION_JSON_VALUE)

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", Matchers.equalTo(expectedTotal.toInt())))
            .andExpect(jsonPath("$.items", Matchers.hasSize<Int>(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(genre.id.value)))
            .andExpect(jsonPath("$.items[0].name", Matchers.equalTo(genre.name)))
            .andExpect(jsonPath("$.items[0].is_active", Matchers.equalTo(genre.active)))
            .andExpect(jsonPath("$.items[0].created_at", Matchers.equalTo(genre.createdAt.toString())))

        verify(listGenresUseCase, times(1)).execute(argThat { input ->
            Objects.equals(input.page, expectedPage)
                    && Objects.equals(input.perPage, expectedPerPage)
                    && Objects.equals(input.sort, expectedSort)
                    && Objects.equals(input.term, expectedTerms)
        })
    }
}