package com.fullcycle.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("ERROR")
data class VideoEncoderError(
    val message: VideoMessage,
    val error: String,
) : VideoEncoderResult {

    companion object {

        private const val ERROR = "ERROR"

    }

    override fun getStatus(): String {
        return ERROR
    }


}
