package com.fullcycle.admin.catalogo.application.category.update

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

class DefaultUpdateCategoryUseCase(
    private val categoryGateway: CategoryGateway
): UpdateCategoryUseCase() {
    override fun execute(anIn: UpdateCategoryCommand): Either<Notification, UpdateCategoryOutput> {
        val currentCategory = findCategory(anIn)

        val notification = Notification.create()

        currentCategory.update(anIn.name, anIn.description, anIn.isActive)
            .validate(notification)

        return if (notification.hasError()) {
            Left(notification)
        } else {
            return update(currentCategory)
        }
    }

    private fun findCategory(command: UpdateCategoryCommand) =
        categoryGateway.findById(command.id).toOption()
            .fold({ throw NotFoundException.with(Category::class, CategoryID.from(command.id)) }, { it })

    private fun update(category: Category): Either<Notification, UpdateCategoryOutput> {
        return Either
            .catch { categoryGateway.update(category) }
            .bimap(Notification::create, UpdateCategoryOutput::from)
    }
}