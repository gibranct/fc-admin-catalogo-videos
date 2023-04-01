package com.fullcycle.admin.catalogo.application.video.media.update

import com.fullcycle.admin.catalogo.domain.video.MediaStatus


data class UpdateMediaStatusCommand(
    val status: MediaStatus,
    val videoId: String,
    val resourceId: String,
    val folder: String,
    val filename: String
) {

    companion object {

        fun with(
            status: MediaStatus,
            videoId: String,
            resourceId: String,
            folder: String,
            filename: String
        ): UpdateMediaStatusCommand {
            return UpdateMediaStatusCommand(status, videoId, resourceId, folder, filename)
        }

    }

}
