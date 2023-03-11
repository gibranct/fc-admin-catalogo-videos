package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.ValueObject
import java.util.*

data class AudioVideoMedia(
    val id: String,
    val checksum: String,
    val name: String,
    val rawLocation: String,
    val encodedLocation: String,
    val status: MediaStatus
) : ValueObject() {


    companion object {

        fun with(
            checkSum: String,
            name: String,
            rawLocation: String,
        ) = AudioVideoMedia(UUID.randomUUID().toString(), checkSum, name, rawLocation, "", MediaStatus.PENDING)

        fun with(
            id: String,
            checksum: String,
            name: String,
            rawLocation: String,
            encodedLocation: String,
            status: MediaStatus
        ): AudioVideoMedia {
            return AudioVideoMedia(id, checksum, name, rawLocation, encodedLocation, status)
        }

    }

    fun processing(): AudioVideoMedia {
        return with(
            id,
            checksum,
            name,
            rawLocation,
            encodedLocation,
            MediaStatus.PROCESSING
        )
    }

    fun completed(encodedPath: String): AudioVideoMedia {
        return with(
            id,
            checksum,
            name,
            rawLocation,
            encodedPath,
            MediaStatus.COMPLETED
        )
    }

    fun isPendingEncode(): Boolean {
        return MediaStatus.PENDING === status
    }

}
