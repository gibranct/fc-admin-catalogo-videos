package com.fullcycle.admin.catalogo.application.category.create

import arrow.core.Either
import arrow.core.Either.Left
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

class DefaultCreateCategoryUseCase(
    private val categoryGateway: CategoryGateway
): CreateCategoryUseCase() {
    override fun execute(anIn: CreateCategoryCommand): Either<Notification, CreateCategoryOutput> {
        val category = Category.newCategory(
            anIn.name,
            anIn.description,
            anIn.isActive,
        )

        val notification = Notification.create()

        category.validate(notification)

        return if (notification.hasError()) Left(notification)
        else create(category)
    }

    private fun create(category: Category): Either<Notification, CreateCategoryOutput> {
        return Either
            .catch { categoryGateway.create(category) }
            .bimap(Notification::create, CreateCategoryOutput::from)
    }
}