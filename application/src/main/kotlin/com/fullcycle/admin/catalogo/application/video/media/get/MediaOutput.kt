package com.fullcycle.admin.catalogo.application.video.media.get

import com.fullcycle.admin.catalogo.domain.resource.Resource

data class MediaOutput(
    val content: ByteArray,
    val contentType: String,
    val name: String,
) {

    companion object {

        fun with(resource: Resource) = MediaOutput(resource.content, resource.contentType, resource.name)

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaOutput

        if (contentType != other.contentType) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        var result = contentType.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
