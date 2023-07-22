package com.fullcycle.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fullcycle.admin.catalogo.ControllerTest
import com.fullcycle.admin.catalogo.Fixture
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaCommand
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaUseCase
import com.fullcycle.admin.catalogo.application.video.media.get.MediaOutput
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaCommand
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaOutput
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.get.VideoOutput
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideosUseCase
import com.fullcycle.admin.catalogo.application.video.retrieve.list.VideoListOutput
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoCommand
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.video.*
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoRequest
import com.google.common.net.HttpHeaders.*
import com.nhaarman.mockitokotlin2.*
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
import kotlin.Any
import kotlin.Exception
import kotlin.Int
import kotlin.Throws


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

    @MockBean
    private lateinit var updateVideoUseCase: UpdateVideoUseCase

    @MockBean
    private lateinit var deleteVideoUseCase: DeleteVideoUseCase

    @MockBean
    private lateinit var getMediaUseCase: GetMediaUseCase

    @MockBean
    private lateinit var uploadMediaUseCase: UploadMediaUseCase

    @Captor
    private lateinit var captor: ArgumentCaptor<VideoSearchQuery>

    @Captor
    private lateinit var createCaptor: ArgumentCaptor<CreateVideoCommand>

    @Captor
    private lateinit var updateCaptor: ArgumentCaptor<UpdateVideoCommand>

    @Captor
    private lateinit var getMediaCaptor: ArgumentCaptor<GetMediaCommand>

    @Captor
    private lateinit var uploadMediaCaptor: ArgumentCaptor<UploadMediaCommand>

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


    @Test
    @Throws(java.lang.Exception::class)
    fun givenAValidId_whenCallsGetById_shouldReturnVideo() {
        // given
        val wesley = Fixture.Companion.CastMembers.wesley()
        val aulas = Fixture.Companion.Categories.aulas()
        val tech = Fixture.Companion.Genres.tech()
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
        val expectedVideo = Fixture.Companion.Videos.audioVideo(VideoMediaType.VIDEO)
        val expectedTrailer = Fixture.Companion.Videos.audioVideo(VideoMediaType.TRAILER)
        val expectedBanner = Fixture.Companion.Videos.image(VideoMediaType.BANNER)
        val expectedThumb = Fixture.Companion.Videos.image(VideoMediaType.THUMBNAIL)
        val expectedThumbHalf = Fixture.Companion.Videos.image(VideoMediaType.THUMBNAIL_HALF)
        val aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories.map(CategoryID::from).toSet(),
            expectedGenres.map(GenreID::from).toSet(),
            expectedMembers.map(CastMemberID::from).toSet(),
        )
            .updateVideoMedia(expectedVideo)
            .updateTrailerMedia(expectedTrailer)
            .updateBannerMedia(expectedBanner)
            .updateThumbnailMedia(expectedThumb)
            .updateThumbnailHalfMedia(expectedThumbHalf)
        val expectedId = aVideo.id.value
        `when`(getVideoByIdUseCase.execute(any()))
            .thenReturn(VideoOutput.from(aVideo))

        // when
        val aRequest = get("/videos/{id}", expectedId)
            .accept(MediaType.APPLICATION_JSON)
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)))
            .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
            .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
            .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear)))
            .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
            .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
            .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
            .andExpect(jsonPath("$.rating", equalTo(expectedRating.name)))
            .andExpect(jsonPath("$.created_at", equalTo(aVideo.createdAt.toString())))
            .andExpect(jsonPath("$.updated_at", equalTo(aVideo.updatedAt.toString())))
            .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id)))
            .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name)))
            .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location)))
            .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum)))
            .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id)))
            .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name)))
            .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location)))
            .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum)))
            .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id)))
            .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name)))
            .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location)))
            .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum)))
            .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id)))
            .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name)))
            .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum)))
            .andExpect(jsonPath("$.video.raw_location", equalTo(expectedVideo.rawLocation)))
            .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation)))
            .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status.name)))
            .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id)))
            .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name)))
            .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum)))
            .andExpect(jsonPath("$.trailer.raw_location", equalTo(expectedTrailer.rawLocation)))
            .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation)))
            .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status.name)))
            .andExpect(jsonPath("$.categories_id", equalTo(ArrayList<Any?>(expectedCategories))))
            .andExpect(jsonPath("$.genres_id", equalTo(ArrayList<Any?>(expectedGenres))))
            .andExpect(jsonPath("$.cast_members_id", equalTo(ArrayList<Any?>(expectedMembers))))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAnInvalidId_whenCallsGetById_shouldReturnNotFound() {
        // given
        val expectedId = VideoID.unique()
        val expectedErrorMessage = "Video with id ${expectedId.value} not found"
        whenever(getVideoByIdUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Video::class, expectedId))

        // when
        val aRequest = get("/videos/{id}", expectedId)
            .accept(MediaType.APPLICATION_JSON)
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {
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
        val aCmd = UpdateVideoRequest(
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
        whenever(updateVideoUseCase.execute(any()))
            .thenReturn(UpdateVideoOutput(expectedId.value))

        // when
        val aRequest = put("/videos/{id}", expectedId.value)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCmd))
        mvc.perform(aRequest)
            .andExpect(status().isOk())
            .andExpect(header().string("Location", "/videos/" + expectedId.value))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.value)))

        // then
        verify(updateVideoUseCase).execute(capture(updateCaptor))
        val actualCmd = updateCaptor.value
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
    fun givenAnInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() {
        // given
        val wesley = Fixture.Companion.CastMembers.wesley()
        val aulas = Fixture.Companion.Categories.aulas()
        val tech = Fixture.Companion.Genres.tech()
        val expectedId = VideoID.unique()
        val expectedErrorMessage = "'title' should not be empty"
        val expectedErrorCount = 1
        val expectedTitle = ""
        val expectedDescription = Fixture.Companion.Videos.description()
        val expectedLaunchYear = Fixture.year()
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Companion.Videos.rating()
        val expectedCategories = setOf(aulas.id.value)
        val expectedGenres = setOf(tech.id.value)
        val expectedMembers = setOf(wesley.id.value)
        val aCmd = UpdateVideoRequest(
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
        whenever(updateVideoUseCase.execute(any()))
            .thenThrow(DomainException.with(Error(expectedErrorMessage)))

        // when
        val aRequest = put("/videos/{id}", expectedId.value)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCmd))
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
            .andExpect(jsonPath("$.errors", hasSize<Int>(expectedErrorCount)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)))
        verify(updateVideoUseCase).execute(any())
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        val expectedId = VideoID.unique()
        doNothing().`when`(deleteVideoUseCase).execute(any())

        // when
        val aRequest = delete("/videos/{id}", expectedId.value)
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isNoContent())
        verify(deleteVideoUseCase).execute(eq(expectedId.value))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() {
        // given
        val expectedId = VideoID.unique()
        val expectedMediaType = VideoMediaType.VIDEO
        val expectedResource = Fixture.Companion.Videos.resource(expectedMediaType)
        val expectedMedia = MediaOutput(expectedResource.content, expectedResource.contentType, expectedResource.name)
        whenever(getMediaUseCase.execute(any())).thenReturn(expectedMedia)

        // when
        val aRequest = get("/videos/${expectedId.value}/medias/${expectedMediaType.name}")
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE, expectedMedia.contentType))
            .andExpect(header().string(CONTENT_LENGTH, expectedMedia.content.size.toString()))
            .andExpect(header().string(CONTENT_DISPOSITION, "attachment; filename=${expectedMedia.name}"))
            .andExpect(content().bytes(expectedMedia.content))

        verify(this.getMediaUseCase).execute(capture(getMediaCaptor))
        val actualCmd = getMediaCaptor.value
        Assertions.assertEquals(expectedId.value, actualCmd.videoId)
        Assertions.assertEquals(expectedMediaType.name, actualCmd.mediaType)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() {
        // given
        val expectedId = VideoID.unique()
        val expectedType = VideoMediaType.VIDEO
        val expectedResource = Fixture.Companion.Videos.resource(expectedType)
        val expectedVideo = MockMultipartFile(
            "media_file",
            expectedResource.name,
            expectedResource.contentType,
            expectedResource.content
        )
        whenever(uploadMediaUseCase.execute(any())).thenReturn(UploadMediaOutput(expectedId.value, expectedType))

        // when
        val aRequest = multipart("/videos/${expectedId.value}/medias/${expectedType.name}")
            .file(expectedVideo)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isCreated())
            .andExpect(
                header().string(
                    LOCATION,
                    "/videos/${expectedId.value}/medias/${expectedType.name}"
                )
            )
            .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.video_id", equalTo(expectedId.value)))
            .andExpect(jsonPath("$.media_type", equalTo(expectedType.name)))

        verify(this.uploadMediaUseCase).execute(capture(uploadMediaCaptor))
        val actualCmd = uploadMediaCaptor.value
        Assertions.assertEquals(expectedId.value, actualCmd.videoId)
        Assertions.assertEquals(expectedResource.content, actualCmd.videoResource.resource.content)
        Assertions.assertEquals(expectedResource.name, actualCmd.videoResource.resource.name)
        Assertions.assertEquals(expectedResource.contentType, actualCmd.videoResource.resource.contentType)
        Assertions.assertEquals(expectedType, actualCmd.videoResource.type)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun givenAnInvalidMediaType_whenCallsUploadMedia_shouldReturnError() {
        // given
        val expectedId = VideoID.unique()
        val expectedResource = Fixture.Companion.Videos.resource(VideoMediaType.VIDEO)
        val expectedVideo = MockMultipartFile(
            "media_file",
            expectedResource.name,
            expectedResource.contentType,
            expectedResource.content,
        )

        // when
        val aRequest = multipart("/videos/${expectedId.value}/medias/INVALID")
            .file(expectedVideo)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        val response = mvc.perform(aRequest)

        // then
        response.andExpect(status().isUnprocessableEntity())
            .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo("Invalid INVALID for VideoMediaType")))
    }
}