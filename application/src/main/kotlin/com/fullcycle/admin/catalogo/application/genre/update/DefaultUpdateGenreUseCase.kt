package com.fullcycle.admin.catalogo.application.genre.update

import arrow.core.toOption
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

data class DefaultUpdateGenreUseCase(
    private val genreGateway: GenreGateway,
    private val categoryGateway: CategoryGateway
): UpdateGenreUseCase() {

    override fun execute(anIn: UpdateGenreCommand): UpdateGenreOutput {
        val genre = findGenre(anIn)
        val name = anIn.name
        val isActive = anIn.isActive
        val categoriesIds = anIn.categoriesIds.map(this::toCategoryId)

        val notification = Notification.create()
        notification.validate(validateCategories(categoriesIds))
        notification.validate { genre.update(name, isActive, categoriesIds) }

        if (notification.hasError()) {
            throw NotificationException("Could not update Aggregate Genre ${genre.id.value}", notification)
        }

        return UpdateGenreOutput.from(genreGateway.update(genre))
    }

    private fun validateCategories(categoryIds: List<CategoryID>): ValidationHandler {
        val notification = Notification.create()
        if (categoryIds.isEmpty()) {
            return notification
        }

        val retrievedIds = this.categoryGateway.existsByIds(categoryIds)
        if (categoryIds.size != retrievedIds.size) {
            val missingIds = ArrayList<CategoryID>(categoryIds)
            missingIds.removeAll(retrievedIds.toSet())
            val missingIdsMessage = missingIds.joinToString(",") { it.value }

            notification.append(Error("Some categories could not be found: $missingIdsMessage"))
        }

        return notification

    }

    private fun findGenre(command: UpdateGenreCommand) =
        genreGateway.findById(command.id).toOption()
            .fold({ throw NotFoundException.with(Genre::class, GenreID.from(command.id)) }, { it })

    private fun toCategoryId(id: String): CategoryID {
        return CategoryID.from(id)
    }
}