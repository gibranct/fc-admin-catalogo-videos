package com.fullcycle.admin.catalogo.domain.genre

import com.fullcycle.admin.catalogo.domain.AggregateRoot
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification
import java.time.Instant
import java.util.*

data class Genre internal constructor(
    override val id: GenreID,
    var name: String,
    var active: Boolean,
    private var categoriesIds: MutableList<CategoryID>,
    val createdAt: Instant,
    var updatedAt: Instant,
    var deletedAt: Instant?,
): AggregateRoot<GenreID>(id) {

    fun categoriesIds(): MutableList<CategoryID> {
        return Collections.unmodifiableList(this.categoriesIds)
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
            val anId = GenreID.unique()
            val now = Instant.now()
            val deletedAt = if (isActive)  null else now

            val genre = Genre(anId, aName, isActive, mutableListOf(), now, now, deletedAt)

            validate(genre)

            return genre
        }


        fun with(
            id: GenreID,
            name: String,
            active: Boolean,
            categoriesIds: MutableList<CategoryID>,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?,
        ): Genre {
            val newList = mutableListOf<CategoryID>()
            newList.addAll(categoriesIds)

            val genre = Genre(id, name, active, newList, createdAt, updatedAt, deletedAt)

            validate(genre)

            return genre
        }

    }

    override fun validate(handler: ValidationHandler) {
        GenreValidator(this, handler).validate()
    }

    fun deactivate(): Genre {
        if (this.deletedAt == null) {
            this.deletedAt = InstantUtils.now()
        }
        this.active = false
        this.updatedAt = InstantUtils.now()
        return this
    }

    fun activate(): Genre {
        this.deletedAt = null
        this.active = true
        this.updatedAt = InstantUtils.now()
        return this
    }

    fun update(aName: String, isActive: Boolean, categoriesIds: List<CategoryID>): Genre {
        if (isActive) activate() else deactivate()

        this.name = aName
        this.updatedAt = InstantUtils.now()
        this.categoriesIds = categoriesIds.toMutableList()

        Companion.validate(this)

        return this
    }

    fun addCategoryId(categoryID: CategoryID): Genre {
        this.categoriesIds.add(categoryID)
        this.updatedAt = InstantUtils.now()
        return this
    }

    fun addCategoriesIds(categoriesIDs: List<CategoryID>): Genre {
        this.categoriesIds.addAll(categoriesIDs)
        this.updatedAt = InstantUtils.now()
        return this
    }

    fun removeCategoryId(categoryID: CategoryID): Genre {
        this.categoriesIds.remove(categoryID)
        this.updatedAt = InstantUtils.now()
        return this
    }
}