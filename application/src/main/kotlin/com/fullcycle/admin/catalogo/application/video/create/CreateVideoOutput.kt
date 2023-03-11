package com.fullcycle.admin.catalogo.application.video.create

import com.fullcycle.admin.catalogo.domain.video.Video


data class CreateVideoOutput(val id: String) {

    companion object {

        fun from(aVideo: Video): CreateVideoOutput {
            return CreateVideoOutput(aVideo.id.value)
        }

    }

}
