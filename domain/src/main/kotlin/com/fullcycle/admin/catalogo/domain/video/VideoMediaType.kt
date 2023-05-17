package com.fullcycle.admin.catalogo.domain.video

enum class VideoMediaType {

    VIDEO,
    TRAILER,
    BANNER,
    THUMBNAIL,
    THUMBNAIL_HALF;

    companion object {
        private val map = VideoMediaType.values().associateBy { it.name }
        operator fun get(value: String) = map[value]
    }
}