package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.ValueObject

data class ImageMedia(
    val id: String,
    val checksum: String,
    val name: String,
    val location: String,
) : ValueObject() {


    companion object {

        fun with(
            id: String,
            checkSum: String,
            name: String,
            location: String,
        ): ImageMedia {
            return ImageMedia(
                id = id,
                checksum = checkSum,
                name = name,
                location = location
            )
        }

    }

}
