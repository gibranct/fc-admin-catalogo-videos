package com.fullcycle.admin.catalogo.application.video.media.upload

import com.fullcycle.admin.catalogo.domain.video.Video
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType


data class UploadMediaOutput(
    val videoId: String,
    val mediaType: VideoMediaType,
) {

    companion object {

        fun with(aVideo: Video, aType: VideoMediaType): UploadMediaOutput {
            return UploadMediaOutput(aVideo.id.value, aType)
        }

    }

}
