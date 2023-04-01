package com.fullcycle.admin.catalogo.application.video.media.upload

import com.fullcycle.admin.catalogo.domain.video.VideoResource


data class UploadMediaCommand(
    val videoId: String,
    val videoResource: VideoResource,
) {

    companion object {

        fun with(anId: String, aResource: VideoResource): UploadMediaCommand {
            return UploadMediaCommand(anId, aResource)
        }

    }

}
