package com.fullcycle.admin.catalogo.infrastructure.genre.persistence

import com.fullcycle.admin.catalogo.domain.category.CategoryID
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table

@Entity
@Table(name = "genres_categories")
data class GenreCategoryJpaEntity(
    @EmbeddedId var id: GenreCategoryID,
    @ManyToOne @MapsId("genreId") var genre: GenreJpaEntity,
) {

    internal constructor(genre: GenreJpaEntity, categoryID: CategoryID) : this(
        GenreCategoryID.from(
            genreId = genre.id,
            categoryId = categoryID.value
        ), genre) {
        this.id = GenreCategoryID.from(genreId = genre.id, categoryId = categoryID.value)
        this.genre = genre
    }

    companion object {

        fun from(genre: GenreJpaEntity, categoryID: CategoryID): GenreCategoryJpaEntity {
            return GenreCategoryJpaEntity(genre = genre, categoryID = categoryID)
        }

    }

}