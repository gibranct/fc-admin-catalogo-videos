package com.fullcycle.admin.catalogo.domain.genre

import com.fullcycle.admin.catalogo.domain.Identifier
import java.util.*

class GenreId(
    override val value: String
): Identifier(value) {

    companion object {
        fun unique(): GenreId {
            return from(UUID.randomUUID().toString().lowercase())
        }

        fun from(anId: String): GenreId {
            return GenreId(anId)
        }

        fun from(anId: UUID): GenreId {
            return GenreId(anId.toString().lowercase())
        }
    }
}