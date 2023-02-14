package com.fullcycle.admin.catalogo.application.castmember.retrieve.get

import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException

class DefaultGetCastMemberByIdUseCase(
    private val castMemberGateway: CastMemberGateway
) : GetCastMemberByIdUseCase() {
    override fun execute(anIn: String): CastMemberOutput {
        val castMemberID = CastMemberID.from(anIn)
        return castMemberGateway.findById(castMemberID)
            .toOption()
            .fold(notFound(castMemberID), CastMemberOutput::from)
    }

    private fun notFound(castMemberID: CastMemberID): () -> Nothing {
        return {
            throw NotFoundException.with(CastMember::class, castMemberID)
        }
    }
}