package com.fullcycle.admin.catalogo.infrastructure.api

import com.fullcycle.admin.catalogo.ControllerTest
import com.fullcycle.admin.catalogo.Fixture
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideosUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.list.VideoListOutput
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.video.VideoPreview
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ControllerTest(controllers = [VideoAPI::class])
class VideoAPITest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var listVideosUseCase: ListVideosUseCase

    @Captor
    private lateinit var captor: ArgumentCaptor<VideoSearchQuery>

    @Test
    fun givenValidParams_whenCallsListVideos_shouldReturnPagination() {
        // given
        val aVideo = VideoPreview(Fixture.video())
        val expectedPage = 50
        val expectedPerPage = 50
        val expectedTerms = "Algo"
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedCastMembers = "cast1"
        val expectedGenres = "gen1"
        val expectedCategories = "cat1"
        val expectedItemsCount = 1
        val expectedTotal = 1L
        val expectedItems = listOf(VideoListOutput.from(aVideo))
        whenever(listVideosUseCase.execute(any()))
            .thenReturn(Pagination(expectedPage, expectedPerPage, expectedTotal, expectedItems))

        // when
        val aRequest = get("/videos")
            .queryParam("page", expectedPage.toString())
            .queryParam("perPage", expectedPerPage.toString())
            .queryParam("sort", expectedSort)
            .queryParam("dir", expectedDirection)
            .queryParam("search", expectedTerms)
            .queryParam("cast_members_ids", expectedCastMembers)
            .queryParam("categories_ids", expectedCategories)
            .queryParam("genres_ids", expectedGenres)
            .accept(MediaType.APPLICATION_JSON)
        val response = this.mvc.perform(aRequest)

        // then
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal.toInt())))
            .andExpect(jsonPath("$.items", hasSize<Int>(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id)))
            .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title)))
            .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description)))
            .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt.toString())))
            .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt.toString())))

        verify(listVideosUseCase).execute(capture(captor))
        val actualQuery = captor.value
        Assertions.assertEquals(expectedPage, actualQuery.page)
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage)
        Assertions.assertEquals(expectedDirection, actualQuery.direction)
        Assertions.assertEquals(expectedSort, actualQuery.sort)
        Assertions.assertEquals(expectedTerms, actualQuery.terms)
        Assertions.assertEquals(setOf(CategoryID.from(expectedCategories)), actualQuery.categories)
        Assertions.assertEquals(setOf(CastMemberID.from(expectedCastMembers)), actualQuery.castMembers)
        Assertions.assertEquals(setOf(GenreID.from(expectedGenres)), actualQuery.genres)
    }

    @Test
    @Throws(Exception::class)
    fun givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() {
        // given
        val aVideo = VideoPreview(Fixture.video())
        val expectedPage = 0
        val expectedPerPage = 25
        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedItemsCount = 1
        val expectedTotal = 1
        val expectedItems = listOf(VideoListOutput.from(aVideo))
        whenever(listVideosUseCase.execute(any()))
            .thenReturn(Pagination(expectedPage, expectedPerPage, expectedTotal.toLong(), expectedItems))

        // when
        val aRequest: MockHttpServletRequestBuilder = get("/videos").accept(MediaType.APPLICATION_JSON)
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize<Int>(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id)))
            .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title)))
            .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description)))
            .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt.toString())))
            .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt.toString())))

        verify(listVideosUseCase).execute(capture(captor))
        val actualQuery = captor.value
        Assertions.assertEquals(expectedPage, actualQuery.page)
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage)
        Assertions.assertEquals(expectedDirection, actualQuery.direction)
        Assertions.assertEquals(expectedSort, actualQuery.sort)
        Assertions.assertEquals(expectedTerms, actualQuery.terms)
        Assertions.assertTrue(actualQuery.categories.isEmpty())
        Assertions.assertTrue(actualQuery.castMembers.isEmpty())
        Assertions.assertTrue(actualQuery.genres.isEmpty())
    }
}