package com.fullcycle.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JsonSubTypes

@Target
@Retention(AnnotationRetention.RUNTIME)
@JacksonAnnotationsInside
@JsonSubTypes(
    JsonSubTypes.Type(value = VideoEncoderCompleted::class),
    JsonSubTypes.Type(value = VideoEncoderError::class),
)
annotation class VideoResponseTypes