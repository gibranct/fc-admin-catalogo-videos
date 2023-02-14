package com.fullcycle.admin.catalogo.application.castmember.retrieve.list

import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType
import java.time.Instant

data class CastMemberListOutput(
    val id: String,
    val name: String,
    val type: CastMemberType,
    val createdAt: Instant,
) {

    companion object {

        fun from(aMember: CastMember): CastMemberListOutput {
            return CastMemberListOutput(
                aMember.id.value,
                aMember.name,
                aMember.type,
                aMember.createdAt,
            )
        }

    }

}