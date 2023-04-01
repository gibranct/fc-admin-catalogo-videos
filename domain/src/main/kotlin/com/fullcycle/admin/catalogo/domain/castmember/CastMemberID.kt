package com.fullcycle.admin.catalogo.domain.castmember

import com.fullcycle.admin.catalogo.domain.Identifier
import com.fullcycle.admin.catalogo.domain.utils.IdUtils
import java.util.*


data class CastMemberID(
    override val value: String
): Identifier(value) {


    companion object {
        fun unique(): CastMemberID {
            return from(IdUtils.uuid())
        }

        fun from(anId: String): CastMemberID {
            return CastMemberID(anId)
        }

        fun from(anId: UUID): CastMemberID {
            return CastMemberID(anId.toString().lowercase())
        }
    }

}