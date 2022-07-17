package com.fullcycle.admin.catalogo.domain.category

import com.fullcycle.admin.catalogo.domain.Identifier
import java.util.UUID

class CategoryID(
    override val value: String
): Identifier(value) {

    companion object {
        fun unique(): CategoryID {
            return from(UUID.randomUUID().toString().lowercase())
        }

        fun from(anId: String): CategoryID {
            return CategoryID(anId)
        }

        fun from(anId: UUID): CategoryID {
            return CategoryID(anId.toString().lowercase())
        }
    }

}