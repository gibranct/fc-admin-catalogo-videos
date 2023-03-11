package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.category.CategoryID
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table


@Entity(name = "VideoCategory")
@Table(name = "videos_categories")
data class VideoCategoryJpaEntity(
    @EmbeddedId val id: VideoCategoryID,
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("videoId") val video: VideoJpaEntity,
) {

    companion object {

        fun from(video: VideoJpaEntity, category: CategoryID): VideoCategoryJpaEntity {
            return VideoCategoryJpaEntity(
                VideoCategoryID.from(video.id, category.value),
                video
            )
        }

    }

}
