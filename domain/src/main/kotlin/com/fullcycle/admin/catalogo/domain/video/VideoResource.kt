package com.fullcycle.admin.catalogo.domain.video

data class VideoResource (
    val type: VideoMediaType,
    val resource: Resource,
) {

    companion object {

        fun with(
            type: VideoMediaType,
            resource: Resource,
        ) = VideoResource(type, resource)

    }

}
