package com.fullcycle.admin.catalogo.application.castmember.create

import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID

data class CreateCastMemberOutput(
    val id: String
) {

    companion object {

        fun from(anId: CastMemberID): CreateCastMemberOutput {
            return CreateCastMemberOutput(anId.value)
        }

        fun from(aMember: CastMember): CreateCastMemberOutput {
            return CreateCastMemberOutput(aMember.id.value)
        }

    }
}