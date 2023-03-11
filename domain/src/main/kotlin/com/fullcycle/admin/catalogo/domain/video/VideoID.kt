package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.Identifier
import java.util.UUID

data class VideoID (
    override val value: String
): Identifier(value) {

    companion object {
        fun unique(): VideoID {
            return from(UUID.randomUUID().toString().lowercase())
        }

        fun from(anId: String): VideoID {
            return VideoID(anId)
        }

        fun from(anId: UUID): VideoID {
            return VideoID(anId.toString().lowercase())
        }
    }

}