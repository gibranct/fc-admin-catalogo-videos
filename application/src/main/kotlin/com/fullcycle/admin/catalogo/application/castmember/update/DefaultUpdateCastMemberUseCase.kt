package com.fullcycle.admin.catalogo.application.castmember.update

import arrow.core.Either
import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.castmember.CastMember
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

class DefaultUpdateCastMemberUseCase(
    private val castMemberGateway: CastMemberGateway
) : UpdateCastMemberUseCase() {
    override fun execute(anIn: UpdateCastMemberCommand): Either<Notification, UpdateCastMemberOutput> {
        val currentCastMember = findCastMember(anIn)

        val notification = Notification.create()

        currentCastMember.update(anIn.name, anIn.type)
            .validate(notification)

        return if (notification.hasError()) {
            Either.Left(notification)
        } else {
            return update(currentCastMember)
        }
    }

    private fun findCastMember(command: UpdateCastMemberCommand) =
        castMemberGateway.findById(CastMemberID.from(command.id)).toOption()
            .fold({ throw NotFoundException.with(CastMember::class, CastMemberID.from(command.id)) }, { it })

    private fun update(castMember: CastMember): Either<Notification, UpdateCastMemberOutput> {
        return Either
            .catch { castMemberGateway.update(castMember) }
            .bimap(Notification::create, UpdateCastMemberOutput::from)
    }
}