package com.fullcycle.admin.catalogo.domain.castmember

import com.fullcycle.admin.catalogo.domain.Identifier
import java.util.*


data class CastMemberID(
    override val value: String
): Identifier(value) {


    companion object {
        fun unique(): CastMemberID {
            return from(UUID.randomUUID().toString().lowercase())
        }

        fun from(anId: String): CastMemberID {
            return CastMemberID(anId)
        }

        fun from(anId: UUID): CastMemberID {
            return CastMemberID(anId.toString().lowercase())
        }
    }

}