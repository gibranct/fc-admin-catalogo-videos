package com.fullcycle.admin.catalogo.application.video.retrieve.list

import com.fullcycle.admin.catalogo.domain.video.Video
import com.fullcycle.admin.catalogo.domain.video.VideoPreview
import java.time.Instant


data class VideoListOutput(
    val  id: String,
    val  title: String,
    val  description: String,
    val  createdAt: Instant,
    val  updatedA: Instant,
) {

    companion object {

        fun from(aVideo: Video): VideoListOutput {
            return VideoListOutput(
                aVideo.id.value,
                aVideo.title,
                aVideo.description,
                aVideo.createdAt,
                aVideo.updatedAt
            )
        }

        fun from(aVideo: VideoPreview): VideoListOutput {
            return VideoListOutput(
                aVideo.id,
                aVideo.title,
                aVideo.description,
                aVideo.createdAt,
                aVideo.updatedAt,
            )
        }

    }

}
