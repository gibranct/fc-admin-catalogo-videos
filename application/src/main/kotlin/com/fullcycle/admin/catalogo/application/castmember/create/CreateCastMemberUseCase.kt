package com.fullcycle.admin.catalogo.application.castmember.create

import arrow.core.Either
import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

abstract class CreateCastMemberUseCase :
    UseCase<CreateCastMemberCommand, Either<Notification, CreateCastMemberOutput>>() {
}