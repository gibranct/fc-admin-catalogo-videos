package com.fullcycle.admin.catalogo.infrastructure.category.persistence

import com.fullcycle.admin.catalogo.domain.category.Category
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "Category")
@Table(name = "category")
class CategoryJpaEntity private constructor(
    @Id val id: String,
    @Column(name = "name", nullable = false) val name: String,
    @Column(name = "description", length = 4000) val description: String?,
    @Column(name = "is_active", nullable = false) val isActive: Boolean,
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)") val createdAt: Instant,
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)") val updatedAt: Instant,
    @Column(name = "deleted_at", nullable = true, columnDefinition = "DATETIME(6)") val deletedAt: Instant?
) {

    companion object {
        fun from(aCategory: Category): CategoryJpaEntity {
            return CategoryJpaEntity(
                aCategory.id.value,
                aCategory.name,
                aCategory.description,
                aCategory.isActive,
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt,
            )
        }
    }

    fun toAggregate(): Category {
        return Category.newCategory(
            CategoryID.from(id),
            name,
            description,
            isActive,
            createdAt,
            updatedAt,
            deletedAt,
        )
    }

}