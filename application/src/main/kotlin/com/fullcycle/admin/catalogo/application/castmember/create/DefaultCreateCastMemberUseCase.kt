package com.fullcycle.admin.catalogo.application.castmember.create

import arrow.core.Either
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

class DefaultCreateCastMemberUseCase(
    private val castMemberGateway: CastMemberGateway
) : CreateCastMemberUseCase() {
    override fun execute(anIn: CreateCastMemberCommand): Either<Notification, CreateCastMemberOutput> {
        val castMember = CastMember.newMember(anIn.name, anIn.type)

        val notification = Notification.create()

        castMember.validate(notification)

        return if (notification.hasError()) Either.Left(notification)
        else create(castMember)
    }

    private fun create(castMember: CastMember): Either<Notification, CreateCastMemberOutput> {
        return Either
            .catch { castMemberGateway.create(castMember) }
            .bimap(Notification::create, CreateCastMemberOutput::from)
    }
}