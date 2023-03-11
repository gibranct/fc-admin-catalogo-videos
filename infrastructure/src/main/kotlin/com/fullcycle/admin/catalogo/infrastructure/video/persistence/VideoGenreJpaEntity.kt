package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.genre.GenreID
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table


@Entity(name = "VideoGenre")
@Table(name = "videos_genres")
data class VideoGenreJpaEntity(
    @EmbeddedId val id: VideoGenreID,
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("videoId") val  video: VideoJpaEntity,
) {


    companion object {

        fun from(video: VideoJpaEntity, genre: GenreID): VideoGenreJpaEntity {
            return VideoGenreJpaEntity(
                VideoGenreID.from(video.id, genre.value),
                video
            )
        }

    }

}
