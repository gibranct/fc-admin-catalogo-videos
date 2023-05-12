package com.fullcycle.admin.catalogo.infrastructure.api.controllers

import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideosUseCase
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.resource.Resource
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoRequest
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoListResponse
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoResponse
import com.fullcycle.admin.catalogo.infrastructure.video.presenters.VideoApiPresenter
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
class VideoController(
    private val listVideosUseCase: ListVideosUseCase,
) : VideoAPI {
    override fun list(
        search: String?,
        page: Int,
        perPage: Int,
        sort: String?,
        direction: String?,
        castMembers: Set<String>?,
        categories: Set<String>?,
        genres: Set<String>?
    ): Pagination<VideoListResponse> {
        val castMemberIDs = castMembers?.map(CastMemberID::from)?.toSet() ?: emptySet()
        val categoriesIDs = categories?.map(CategoryID::from)?.toSet() ?: emptySet()
        val genresIDs = genres?.map(GenreID::from)?.toSet() ?: emptySet()

        val aQuery = VideoSearchQuery(
            page, perPage,
            search!!, sort!!, direction!!, castMemberIDs, categoriesIDs, genresIDs
        )

        return VideoApiPresenter.present(this.listVideosUseCase.execute(aQuery))
    }

    override fun createFull(
        title: String?,
        description: String?,
        yearLaunched: Int?,
        duration: Double?,
        opened: Boolean?,
        published: Boolean?,
        rating: String?,
        categories: Set<String?>?,
        castMembers: Set<String?>?,
        genres: Set<String?>?,
        videoFile: MultipartFile?,
        trailerFile: MultipartFile?,
        bannerFile: MultipartFile?,
        thumbFile: MultipartFile?,
        thumbHalfFile: MultipartFile?
    ): ResponseEntity<*> {
        TODO("Not yet implemented")
    }

    override fun createPartial(payload: CreateVideoRequest?): ResponseEntity<*> {
        TODO("Not yet implemented")
    }

    override fun getById(id: String?): VideoResponse {
        TODO("Not yet implemented")
    }

    override fun update(id: String, payload: UpdateVideoRequest): ResponseEntity<*>? {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: String) {
        TODO("Not yet implemented")
    }

    override fun getMediaByType(id: String, type: String): ResponseEntity<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun uploadMediaByType(id: String, type: String, media: MultipartFile): ResponseEntity<*> {
        TODO("Not yet implemented")
    }

    private fun resourceOf(part: MultipartFile?): Resource? {
        return if (part == null) {
            null
        } else try {
            Resource.with(
                part.bytes,
                HashingUtils.checksum(part.bytes),
                part.contentType!!,
                part.originalFilename!!,
            )
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }
}