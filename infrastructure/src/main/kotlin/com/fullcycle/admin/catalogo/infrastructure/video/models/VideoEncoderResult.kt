package com.fullcycle.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "status")
sealed interface VideoEncoderResult {

    fun getStatus(): String

}

@JsonTypeName("COMPLETED")
data class VideoEncoderCompleted(
    val id: String,
    val outputBucketPath: String,
    val video: VideoMetadata
) : VideoEncoderResult {

    companion object {

        private const val COMPLETED = "COMPLETED"

    }

    override fun getStatus(): String {
        return COMPLETED
    }


}

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