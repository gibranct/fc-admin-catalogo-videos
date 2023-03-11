package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.ValueObject

data class Resource private constructor(
    val content: List<Byte>,
    val checksum: String,
    val contentType: String,
    val name: String
) : ValueObject() {

    companion object {

        fun with(
            content: List<Byte>,
            checksum: String,
            contentType: String,
            name: String
        ) = Resource(content, checksum, contentType, name)

    }

}