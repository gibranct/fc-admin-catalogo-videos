package com.fullcycle.admin.catalogo.infrastructure.video

import com.fullcycle.admin.catalogo.domain.event.DomainEventPublisher
import com.fullcycle.admin.catalogo.domain.pagination.Pagination
import com.fullcycle.admin.catalogo.domain.video.Video
import com.fullcycle.admin.catalogo.domain.video.VideoGateway
import com.fullcycle.admin.catalogo.domain.video.VideoID
import com.fullcycle.admin.catalogo.domain.video.VideoPreview
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery
import com.fullcycle.admin.catalogo.infrastructure.services.EventService
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class DefaultVideoGateway(
    private val videoRepository: VideoRepository,
    private val eventService: EventService,
) : VideoGateway {

    @Transactional
    override fun create(aVideo: Video): Video {
        return save(aVideo)
    }

    override fun deleteById(anId: VideoID) {
        val aVideoId = anId.value
        if (this.videoRepository.existsById(aVideoId)) {
            this.videoRepository.deleteById(aVideoId)
        }
    }

    @Transactional(readOnly = true)
    override fun findById(anId: VideoID): Video? {
        val entityOptional = videoRepository.findById(anId.value)
        return when(entityOptional.isPresent) {
            true -> entityOptional.get().toAggregate()
            false -> null
        }
    }

    @Transactional
    override fun update(aVideo: Video): Video {
        return save(aVideo)
    }

    override fun findAll(aQuery: VideoSearchQuery): Pagination<VideoPreview> {
        val page = PageRequest.of(
            aQuery.page,
            aQuery.perPage,
            Sort.by(Sort.Direction.fromString(aQuery.direction), aQuery.sort)
        )

        val actualPage = videoRepository.findAll(
            aQuery.terms,
            aQuery.castMembers.map { it.value }.toSet(),
            aQuery.categories.map { it.value }.toSet(),
            aQuery.genres.map { it.value }.toSet(),
            page
        )

        return Pagination(
            actualPage.number,
            actualPage.size,
            actualPage.totalElements,
            actualPage.toList()
        )
    }

    private fun save(aVideo: Video): Video {
        val video = videoRepository
            .save(VideoJpaEntity.from(aVideo))
            .toAggregate()

        video.publishDomainEvents(eventService::send)

        return video
    }
}