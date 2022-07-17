package com.fullcycle.admin.catalogo.domain.genre

import com.fullcycle.admin.catalogo.domain.AggregateRoot
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import java.time.Instant
import java.util.Collections

data class Genre internal constructor(
    override val id: GenreId,
    val name: String,
    val active: Boolean,
    val categoriesIds: MutableList<CategoryID>,
    val createdAt: Instant,
    var updatedAt: Instant,
    var deletedAt: Instant?,
): AggregateRoot<GenreId>(id) {

    fun categoriesIds(): MutableList<CategoryID> {
        return Collections.unmodifiableList(categoriesIds)
    }

    companion object {

        private fun validate(genre: Genre) {
            val notification = Notification.create()
            genre.validate(notification)

            if (notification.hasError()) {
                throw NotificationException("", notification)
            }
        }

        fun newGenre(
            aName: String,
            isActive: Boolean,
        ): Genre {
            val anId = GenreId.unique()
            val now = Instant.now()
            val deletedAt = if (isActive)  null else now

            val genre = Genre(anId, aName, isActive, mutableListOf(), now, now, deletedAt)

            validate(genre)

            return genre
        }


        fun with(
            id: GenreId,
            name: String,
            active: Boolean,
            categoriesIds: MutableList<CategoryID>,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?,
        ): Genre {
            val newList = mutableListOf<CategoryID>()
            Collections.copy(newList, categoriesIds)

            val genre = Genre(id, name, active, Collections.unmodifiableList(newList), createdAt, updatedAt, deletedAt)

            validate(genre)

            return genre
        }

    }

    override fun validate(handler: ValidationHandler) {
        GenreValidator(this, handler).validate()
    }
}