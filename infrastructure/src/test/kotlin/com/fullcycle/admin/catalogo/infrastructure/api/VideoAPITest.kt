package com.fullcycle.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fullcycle.admin.catalogo.ControllerTest
import com.fullcycle.admin.catalogo.Fixture
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideosUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.list.VideoListOutput
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.video.VideoID
import com.fullcycle.admin.catalogo.domain.video.VideoPreview
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest
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
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@ControllerTest(controllers = [VideoAPI::class])
class VideoAPITest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    private lateinit var listVideosUseCase: ListVideosUseCase

    @MockBean
    private lateinit var createVideoUseCase: CreateVideoUseCase

    @MockBean
    private lateinit var getVideoByIdUseCase: GetVideoByIdUseCase

    @Captor
    private lateinit var captor: ArgumentCaptor<VideoSearchQuery>

    @Captor
    private lateinit var createCaptor: ArgumentCaptor<CreateVideoCommand>

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

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() {
        // given
        val wesley = Fixture.Companion.CastMembers.wesley()
        val aulas = Fixture.Companion.Categories.aulas()
        val tech = Fixture.Companion.Genres.tech()
        val expectedId = VideoID.unique()
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Companion.Videos.description()
        val expectedLaunchYear = Fixture.year()
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Companion.Videos.rating()
        val expectedCategories = setOf(aulas.id.value)
        val expectedGenres = setOf(tech.id.value)
        val expectedMembers = setOf(wesley.id.value)
        val expectedVideo = MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".toByteArray())
        val expectedTrailer = MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".toByteArray())
        val expectedBanner = MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".toByteArray())
        val expectedThumb = MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".toByteArray())
        val expectedThumbHalf =
            MockMultipartFile("thumb_half_file", "thumbnailHalf.jpg", "image/jpg", "THUMBHALF".toByteArray())
        whenever(createVideoUseCase.execute(any())).thenReturn(CreateVideoOutput(expectedId.value))

        // when
        val aRequest = multipart("/videos")
            .file(expectedVideo)
            .file(expectedTrailer)
            .file(expectedBanner)
            .file(expectedThumb)
            .file(expectedThumbHalf)
            .param("title", expectedTitle)
            .param("description", expectedDescription)
            .param("year_launched", expectedLaunchYear.toString())
            .param("duration", expectedDuration.toString())
            .param("opened", expectedOpened.toString())
            .param("published", expectedPublished.toString())
            .param("rating", expectedRating.name)
            .param("cast_members_id", wesley.id.value)
            .param("categories_id", aulas.id.value)
            .param("genres_id", tech.id.value)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        mvc.perform(aRequest)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/videos/" + expectedId.value))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.value)))

        // then
        verify(createVideoUseCase).execute(capture(createCaptor))
        val actualCmd = createCaptor.value
        Assertions.assertEquals(expectedTitle, actualCmd.title)
        Assertions.assertEquals(expectedDescription, actualCmd.description)
        Assertions.assertEquals(expectedLaunchYear, actualCmd.launchedAt)
        Assertions.assertEquals(expectedDuration, actualCmd.duration)
        Assertions.assertEquals(expectedOpened, actualCmd.opened)
        Assertions.assertEquals(expectedPublished, actualCmd.published)
        Assertions.assertEquals(expectedRating.name, actualCmd.rating)
        Assertions.assertEquals(expectedCategories, actualCmd.categories)
        Assertions.assertEquals(expectedGenres, actualCmd.genres)
        Assertions.assertEquals(expectedMembers, actualCmd.members)
        Assertions.assertEquals(expectedVideo.originalFilename, actualCmd.video?.name)
        Assertions.assertEquals(expectedTrailer.originalFilename, actualCmd.trailer?.name)
        Assertions.assertEquals(expectedBanner.originalFilename, actualCmd.banner?.name)
        Assertions.assertEquals(expectedThumb.originalFilename, actualCmd.thumbnail?.name)
        Assertions.assertEquals(expectedThumbHalf.originalFilename, actualCmd.thumbnailHalf?.name)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAnInvalidCommand_whenCallsCreateFull_shouldReturnError() {
        // given
        // when
        val aRequest = multipart("/videos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.MULTIPART_FORM_DATA)

        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAValidCommand_whenCallsCreatePartial_shouldReturnId() {
        // given
        val wesley = Fixture.Companion.CastMembers.wesley()
        val aulas = Fixture.Companion.Categories.aulas()
        val tech = Fixture.Companion.Genres.tech()
        val expectedId = VideoID.unique()
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Companion.Videos.description()
        val expectedLaunchYear = Fixture.year()
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Companion.Videos.rating()
        val expectedCategories = setOf(aulas.id.value)
        val expectedGenres = setOf(tech.id.value)
        val expectedMembers = setOf(wesley.id.value)
        val aCmd = CreateVideoRequest(
            expectedTitle,
            expectedDescription,
            expectedDuration,
            expectedLaunchYear,
            expectedOpened,
            expectedPublished,
            expectedRating.name,
            expectedMembers,
            expectedCategories,
            expectedGenres
        )
        `when`(createVideoUseCase.execute(any()))
            .thenReturn(CreateVideoOutput(expectedId.value))

        // when
        val aRequest = post("/videos")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCmd))
        mvc.perform(aRequest)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/videos/" + expectedId.value))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.value)))

        // then
        verify(createVideoUseCase).execute(capture(createCaptor))
        val actualCmd = createCaptor.value
        Assertions.assertEquals(expectedTitle, actualCmd.title)
        Assertions.assertEquals(expectedDescription, actualCmd.description)
        Assertions.assertEquals(expectedLaunchYear, actualCmd.launchedAt)
        Assertions.assertEquals(expectedDuration, actualCmd.duration)
        Assertions.assertEquals(expectedOpened, actualCmd.opened)
        Assertions.assertEquals(expectedPublished, actualCmd.published)
        Assertions.assertEquals(expectedRating.name, actualCmd.rating)
        Assertions.assertEquals(expectedCategories, actualCmd.categories)
        Assertions.assertEquals(expectedGenres, actualCmd.genres)
        Assertions.assertEquals(expectedMembers, actualCmd.members)
        Assertions.assertNull(actualCmd.video)
        Assertions.assertNull(actualCmd.trailer)
        Assertions.assertNull(actualCmd.banner)
        Assertions.assertNull(actualCmd.thumbnail)
        Assertions.assertNull(actualCmd.thumbnailHalf)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAnEmptyBody_whenCallsCreatePartial_shouldReturnError() {
        // when
        val aRequest = post("/videos")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isUnprocessableEntity())
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAnInvalidCommand_whenCallsCreatePartial_shouldReturnError() {
        // given
        val expectedErrorMessage = "title is required"
        whenever(createVideoUseCase.execute(any()))
            .thenThrow(DomainException.with(Error(expectedErrorMessage)))

        // when
        val aRequest = post("/videos")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                        {
                          "title": "Olá Mundo!"
                        }
                        
                        """.trimIndent()
            )
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
    }
}