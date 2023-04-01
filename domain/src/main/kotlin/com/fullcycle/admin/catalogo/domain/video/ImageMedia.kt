package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.ValueObject
import com.fullcycle.admin.catalogo.domain.utils.IdUtils
import java.util.UUID

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

        fun with(
            checkSum: String,
            name: String,
            location: String,
        ) = ImageMedia(
                id = IdUtils.uuid(),
                checksum = checkSum,
                name = name,
                location = location
            )


    }

}
