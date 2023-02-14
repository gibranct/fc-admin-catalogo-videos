package com.fullcycle.admin.catalogo.application.genre.create

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

class DefaultCreateGenreUseCase(
    private val genreGateway: GenreGateway,
    private val categoryGateway: CategoryGateway
): CreateGenreUseCase() {
    override fun execute(anIn: CreateGenreCommand): CreateGenreOutput {
        val (name, isActive) = anIn
        val categoryIds = anIn.categoriesIds.map(this::toCategoryId)

        val notification = Notification.create()
        notification.validate(validateCategories(categoryIds))

        val genre = notification.validate { Genre.newGenre(name, isActive) }

        if (notification.hasError()) {
            throw NotificationException("Could not create Aggregate Genre", notification)
        }

        genre?.addCategoriesIds(categoryIds)
        return CreateGenreOutput.from(this.genreGateway.create(genre!!))
    }

    private fun validateCategories(categoryIds: List<CategoryID>): ValidationHandler {
        val notification = Notification.create()
        if (categoryIds.isEmpty()) {
            return notification
        }

        val retrievedIds = this.categoryGateway.existsById(categoryIds)
        if (categoryIds.size != retrievedIds.size) {
            val missingIds = ArrayList<CategoryID>(categoryIds)
            missingIds.removeAll(retrievedIds.toSet())
            val missingIdsMessage = missingIds.joinToString(",") { it.value }

            notification.append(Error("Some categories could not be found: $missingIdsMessage"))
        }

        return notification

    }

    private fun toCategoryId(id: String): CategoryID {
        return CategoryID.from(id)
    }
}