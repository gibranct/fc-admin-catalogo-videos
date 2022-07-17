package com.fullcycle.admin.catalogo.infrastructure.api

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import com.fullcycle.admin.catalogo.ControllerTest
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase
import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutput
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesOutput
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutput
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest
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
import java.util.*

@ControllerTest(controllers = [CategoryAPI::class])
class CategoryAPITest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var createCategoryUseCase: CreateCategoryUseCase

    @MockBean
    private lateinit var getCategoryByIdUseCase: GetCategoryByIdUseCase

    @MockBean
    private lateinit var updateCategoryUseCase: UpdateCategoryUseCase

    @MockBean
    private lateinit var deleteCategoryUseCase: DeleteCategoryUseCase

    @MockBean
    private lateinit var listCategoryUseCase: ListCategoriesUseCase

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        val anInput = CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        Mockito.`when`(createCategoryUseCase.execute(any()))
            .thenReturn(Either.Right(CreateCategoryOutput.from("123")))

        val request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isCreated,
                header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", Matchers.equalTo("123"))
            )

        verify(createCategoryUseCase, times(1)).execute(argThat { command ->
            Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedDescription, command.description)
                    && Objects.equals(expectedIsActive, command.isActive)
        })
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnNotification() {
        val expectedName = ""
        val expectedDescription = "new description"
        val expectedIsActive = true
        val expectedMessage = "'name' should not be null or empty"

        val anInput = CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        Mockito.`when`(createCategoryUseCase.execute(any()))
            .thenReturn(Either.Left(Notification.create(Error(expectedMessage))))

        val request = post("/categories")
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

        verify(createCategoryUseCase, times(1)).execute(argThat { command ->
            Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedDescription, command.description)
                    && Objects.equals(expectedIsActive, command.isActive)
        })
    }

    @Test
    fun givenAnInvalidName_whenCallsCreateCategory_shouldReturnADomainException() {
        val expectedName = ""
        val expectedDescription = "new description"
        val expectedIsActive = true
        val expectedMessage = "'name' should not be null or empty"

        val anInput = CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        Mockito.`when`(createCategoryUseCase.execute(any()))
            .thenThrow(DomainException.with(Error(expectedMessage)))

        val request = post("/categories")
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

        verify(createCategoryUseCase, times(1)).execute(argThat { command ->
            Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedDescription, command.description)
                    && Objects.equals(expectedIsActive, command.isActive)
        })
    }

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true

        val aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        val expectedId = aCategory.id.value

        Mockito.`when`(getCategoryByIdUseCase.execute(any()))
            .thenReturn(CategoryOutput.from(aCategory))

        val request = get("/categories/${expectedId}")
            .accept(MediaType.APPLICATION_JSON_VALUE)

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isOk,
                header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", Matchers.equalTo(expectedId)),
                jsonPath("$.name", Matchers.equalTo(expectedName)),
                jsonPath("$.description", Matchers.equalTo(expectedDescription)),
                jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)),
            )

        verify(getCategoryByIdUseCase, times(1)).execute(argThat { input ->
            Objects.equals(input, expectedId)
        })
    }

    @Test
    fun givenAnInValidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedId = CategoryID.from("123")
        val expectedErrorMessage = "${Category::class.simpleName} with id ${expectedId.value} not found"

        Mockito.`when`(getCategoryByIdUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Category::class, expectedId))

        val request = get("/categories/${expectedId.value}")

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
        val aCategory = Category.newCategory("Film", null, true)

        val expectedName = "valid name"
        val expectedDescription = "new description"
        val expectedIsActive = true
        val expectedId = aCategory.id

        Mockito.`when`(updateCategoryUseCase.execute(any()))
            .thenReturn(Either.Right(UpdateCategoryOutput.from(aCategory)))

        val anInput = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)

        val request = put("/categories/${expectedId.value}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anInput))

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.value)))

        verify(updateCategoryUseCase, times(1)).execute(argThat { command ->
            Objects.equals(command.id, expectedId.value)
                    && Objects.equals(expectedName, command.name)
                    && Objects.equals(expectedDescription, command.description)
                    && Objects.equals(expectedIsActive, command.isActive)
        })

    }

    @Test
    fun givenAnInvalidId_whenCallsUpdateCategory_shouldReturnNotFound() {
        val expectedId = "9999"
        val expectedErrorMessage = "Category with id $expectedId not found"

        Mockito.`when`(updateCategoryUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Category::class, CategoryID.from(expectedId)))

        val anInput = UpdateCategoryRequest("any", "", true)

        val request = put("/categories/${expectedId}")
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

        Mockito.`when`(updateCategoryUseCase.execute(any()))
            .thenReturn(Either.Left(Notification.create(Error(expectedErrorMessage))))

        val anInput = UpdateCategoryRequest("any", "", true)

        val request = put("/categories/${expectedId}")
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

        doNothing().`when`(deleteCategoryUseCase).execute(any())

        val request = delete("/categories/${expectedId}")
            .accept(MediaType.APPLICATION_JSON_VALUE)

        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNoContent)

        verify(deleteCategoryUseCase, times(1)).execute(argThat { input ->
            Objects.equals(input, expectedId)
        })
    }

    @Test
    fun givenValidParams_whenCallsListCategories_shouldReturnCategories() {
        val category = Category.newCategory("Film", null, true)

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedItemsCount = 1
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTerms = "movies"
        val expectedTotal: Long = 1
        val expectedItems = listOf<ListCategoriesOutput>(ListCategoriesOutput.from(category))

        Mockito.`when`(listCategoryUseCase.execute(any()))
            .thenReturn(Pagination(
                currentPage = expectedPage,
                perPage = expectedPerPage,
                total = expectedTotal,
                items = expectedItems
            ))

        val request = get("/categories")
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
            .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(category.id.value)))
            .andExpect(jsonPath("$.items[0].name", Matchers.equalTo(category.name)))
            .andExpect(jsonPath("$.items[0].description", Matchers.equalTo(category.description)))
            .andExpect(jsonPath("$.items[0].is_active", Matchers.equalTo(category.isActive)))
            .andExpect(jsonPath("$.items[0].created_at", Matchers.equalTo(category.createdAt.toString())))
            .andExpect(jsonPath("$.items[0].deleted_at", Matchers.equalTo(category.deletedAt)))

        verify(listCategoryUseCase, times(1)).execute(argThat { input ->
            Objects.equals(input.page, expectedPage)
                    && Objects.equals(input.perPage, expectedPerPage)
                    && Objects.equals(input.sort, expectedSort)
                    && Objects.equals(input.term, expectedTerms)
        })
    }
}