package com.fullcycle.admin.catalogo.application.category.update

import arrow.core.Either
import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

abstract class UpdateCategoryUseCase:
    UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>>() {
}