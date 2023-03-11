package com.fullcycle.admin.catalogo.infrastructure.video.models

sealed interface VideoEncoderResult {

    fun getStatus(): String

}