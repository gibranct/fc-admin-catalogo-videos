package com.fullcycle.admin.catalogo.application.castmember.delete

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID

class DefaultDeleteCastMemberUseCase(
    private val castMemberGateway: CastMemberGateway
) : DeleteCastMemberUseCase() {
    override fun execute(anIn: String) {
        this.castMemberGateway.deleteById(CastMemberID.from(anIn))
    }
}