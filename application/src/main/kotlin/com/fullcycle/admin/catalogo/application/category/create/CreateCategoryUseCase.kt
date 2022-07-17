package com.fullcycle.admin.catalogo.application.category.create

import arrow.core.Either
import com.fullcycle.admin.catalogo.application.UseCase
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

abstract class CreateCategoryUseCase:
    UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>>() {
}