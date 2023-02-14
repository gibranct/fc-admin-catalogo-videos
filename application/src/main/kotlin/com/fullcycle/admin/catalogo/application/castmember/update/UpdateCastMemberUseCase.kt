package com.fullcycle.admin.catalogo.application.castmember.update

import arrow.core.Either
import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

abstract class UpdateCastMemberUseCase : UseCase<UpdateCastMemberCommand, Either<Notification, UpdateCastMemberOutput>>() {
}