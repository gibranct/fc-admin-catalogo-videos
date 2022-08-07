package com.fullcycle.admin.catalogo.domain.category

import com.fullcycle.admin.catalogo.domain.AggregateRoot
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import java.time.Instant

data class Category constructor(
    override val id: CategoryID,
    var name: String,
    var description: String?,
    var isActive: Boolean,
    val createdAt: Instant,
    var updatedAt: Instant,
    var deletedAt: Instant?,
): AggregateRoot<CategoryID>(id) {

    companion object {
        fun newCategory(
            aName: String,
            aDescription: String?,
            isActive: Boolean,
        ): Category {
            val now  = InstantUtils.now()
            val id = CategoryID.unique()
            val deletedAt = if (isActive) null else now
            return newCategory(id, aName, aDescription, isActive, now, now, deletedAt)
        }

        fun newCategory(
            id: CategoryID,
            aName: String,
            aDescription: String?,
            isActive: Boolean,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?,
        ): Category {
            return Category(
                id,
                aName,
                aDescription,
                isActive,
                createdAt,
                updatedAt,
                deletedAt
            )
        }
    }

    override fun validate(handler: ValidationHandler) {
        CategoryValidator(this, handler).validate()
    }

    fun deactivate(): Category {
        if (this.deletedAt == null) {
            this.deletedAt = InstantUtils.now()
        }
        this.isActive = false
        this.updatedAt = InstantUtils.now()
        return this
    }

    fun activate(): Category {
        this.deletedAt = null
        this.isActive = true
        this.updatedAt = InstantUtils.now()
        return this
    }

    fun update(aName: String, aDescription: String, isActive: Boolean): Category {
        if (isActive) activate() else deactivate()

        this.name = aName
        this.description = aDescription
        this.updatedAt = InstantUtils.now()

        return this
    }

}