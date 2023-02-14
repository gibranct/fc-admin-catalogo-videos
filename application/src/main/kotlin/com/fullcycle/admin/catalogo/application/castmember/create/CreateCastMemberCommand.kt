package com.fullcycle.admin.catalogo.application.castmember.create

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType

data class CreateCastMemberCommand(
    val name: String,
    val type: CastMemberType
) {

    companion object {

        fun with(aName: String, aType: CastMemberType): CreateCastMemberCommand {
            return CreateCastMemberCommand(aName, aType)
        }

    }
}