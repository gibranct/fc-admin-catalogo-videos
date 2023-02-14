package com.fullcycle.admin.catalogo.application.castmember.update

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType

data class UpdateCastMemberCommand(
    val id: String,
    val name: String,
    val type: CastMemberType,
) {

    companion object {

        fun with(
            anId: String,
            aName: String,
            aType: CastMemberType,
        ): UpdateCastMemberCommand {
            return UpdateCastMemberCommand(anId, aName, aType)
        }

    }

}