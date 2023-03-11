package com.fullcycle.admin.catalogo.application.video.update

import com.fullcycle.admin.catalogo.domain.video.Video

data class UpdateVideoOutput(val id: String) {

    companion object {

        fun from(video: Video) = UpdateVideoOutput(video.id.value)

    }

}
