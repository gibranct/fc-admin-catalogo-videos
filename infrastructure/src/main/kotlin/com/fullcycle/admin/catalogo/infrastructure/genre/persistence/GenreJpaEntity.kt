package com.fullcycle.admin.catalogo.infrastructure.genre.persistence

import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.Genre
import com.fullcycle.admin.catalogo.domain.genre.GenreID
import java.time.Instant
import java.util.*
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity(name = "Genre")
@Table(name = "genres")
data class GenreJpaEntity(
    @Id() val id: String,
    @Column(name = "name", nullable = false) var name: String,
    @Column(name = "active", nullable = false) var active: Boolean,
    @OneToMany(mappedBy = "genre", cascade = [ALL], fetch = EAGER, orphanRemoval = true) var categories: MutableSet<GenreCategoryJpaEntity> = mutableSetOf(),
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)") var createdAt: Instant,
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)") var updatedAt: Instant,
    @Column(name = "deleted_at", nullable = true, columnDefinition = "DATETIME(6)") var deletedAt: Instant?,
) {

    fun addCategoryId(categoryID: CategoryID) {
        this.categories.add(GenreCategoryJpaEntity.from(this, categoryID))
    }

    fun removeCategoryId(categoryID: CategoryID) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, categoryID))
    }

    fun categoryIDS(): List<CategoryID> {
        return categories.map { CategoryID.from(it.id.categoryId) }
    }

    companion object {

        fun from(genre: Genre): GenreJpaEntity {
            val genreJpaEntity = GenreJpaEntity(
                id = genre.id.value,
                name = genre.name,
                active = genre.active,
                createdAt = genre.createdAt,
                updatedAt = genre.updatedAt,
                deletedAt = genre.deletedAt
            )

            genre.categoriesIds().forEach {
                genreJpaEntity.addCategoryId(it)
            }

            return genreJpaEntity
        }

    }

    fun toAggregate(): Genre {
        return Genre.with(
            id = GenreID.from(this.id),
            name = name,
            active = active,
            categoriesIds = categories.map { CategoryID.from(it.id.categoryId) }.toMutableList(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt
        )
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenreJpaEntity

        if (id != other.id) return false
        return true
    }
}
