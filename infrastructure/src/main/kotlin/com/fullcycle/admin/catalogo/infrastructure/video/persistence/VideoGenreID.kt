package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class VideoGenreID(
    @Column(name = "video_id", nullable = false) val videoId: String,
    @Column(name = "genre_id", nullable = false) val  genreId: String,
) : Serializable {

    companion object {

        fun from(videoId: String, genreId: String): VideoGenreID {
            return VideoGenreID(videoId, genreId)
        }

    }

}
