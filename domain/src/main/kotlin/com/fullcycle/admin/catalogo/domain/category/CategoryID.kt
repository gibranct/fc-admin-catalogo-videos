package com.fullcycle.admin.catalogo.domain.category

import com.fullcycle.admin.catalogo.domain.Identifier
import com.fullcycle.admin.catalogo.domain.utils.IdUtils
import java.util.UUID

data class CategoryID(
    override val value: String
): Identifier(value) {

    companion object {
        fun unique(): CategoryID {
            return from(IdUtils.uuid())
        }

        fun from(anId: String): CategoryID {
            return CategoryID(anId)
        }

        fun from(anId: UUID): CategoryID {
            return CategoryID(anId.toString().lowercase())
        }
    }

}