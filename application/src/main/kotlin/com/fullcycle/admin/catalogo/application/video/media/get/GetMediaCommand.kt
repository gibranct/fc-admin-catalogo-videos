package com.fullcycle.admin.catalogo.application.video.media.get

data class GetMediaCommand(
    val videoId: String,
    val mediaType: String,
) {

    companion object {
        fun with(videoId: String, type: String) = GetMediaCommand(videoId, type)
    }

}

