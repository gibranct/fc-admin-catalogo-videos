package com.fullcycle.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("COMPLETED")
data class VideoEncoderCompleted(
    val id: String,
    val outputBucket: String,
    val video: VideoMetadata
) : VideoEncoderResult {

    companion object {

        private const val COMPLETED = "COMPLETED"

    }

    override fun getStatus(): String {
        return COMPLETED
    }


}
