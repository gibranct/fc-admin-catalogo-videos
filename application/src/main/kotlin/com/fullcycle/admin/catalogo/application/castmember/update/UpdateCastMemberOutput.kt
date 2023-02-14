package com.fullcycle.admin.catalogo.application.castmember.update

import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID

data class UpdateCastMemberOutput(
    val id: String
) {

    companion object {

        fun from(anId: CastMemberID): UpdateCastMemberOutput {
            return UpdateCastMemberOutput(anId.value)
        }

        fun from(aMember: CastMember): UpdateCastMemberOutput {
            return from(aMember.id)
        }

    }

}