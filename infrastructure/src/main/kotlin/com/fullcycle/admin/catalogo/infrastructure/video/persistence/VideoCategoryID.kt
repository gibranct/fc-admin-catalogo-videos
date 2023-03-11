package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class VideoCategoryID(
    @Column(name = "video_id", nullable = false) val videoId: String,
    @Column(name = "category_id", nullable = false) val categoryId: String,
) : Serializable {

    companion object {

        fun from(videoId: String, categoryId: String): VideoCategoryID {
            return VideoCategoryID(videoId, categoryId)
        }

    }

}
