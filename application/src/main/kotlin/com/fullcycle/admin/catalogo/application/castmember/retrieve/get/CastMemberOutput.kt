package com.fullcycle.admin.catalogo.application.castmember.retrieve.get

import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType
import java.time.Instant

data class CastMemberOutput(
    val id: String,
    val name: String,
    val type: CastMemberType,
    val createdAt: Instant,
    val updatedAt: Instant,
) {

    companion object {

        fun from(aMember: CastMember): CastMemberOutput {
            return CastMemberOutput(
                aMember.id.value,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt,
            )
        }

    }

}