package com.fullcycle.admin.catalogo.domain.genre

import com.fullcycle.admin.catalogo.domain.Identifier
import com.fullcycle.admin.catalogo.domain.utils.IdUtils
import java.util.*

data class GenreID(
    override val value: String
): Identifier(value) {

    companion object {
        fun unique(): GenreID {
            return from(IdUtils.uuid())
        }

        fun from(anId: String): GenreID {
            return GenreID(anId)
        }

        fun from(anId: UUID): GenreID {
            return GenreID(anId.toString().lowercase())
        }
    }
}
