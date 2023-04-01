package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.ValueObject
import com.fullcycle.admin.catalogo.domain.resource.Resource

data class VideoResource (
    val type: VideoMediaType,
    val resource: Resource,
) : ValueObject() {

    companion object {

        fun with(
            type: VideoMediaType,
            resource: Resource,
        ) = VideoResource(type, resource)

    }

}
