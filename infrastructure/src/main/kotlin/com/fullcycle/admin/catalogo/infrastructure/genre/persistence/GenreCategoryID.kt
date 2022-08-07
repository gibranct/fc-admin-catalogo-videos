package com.fullcycle.admin.catalogo.infrastructure.genre.persistence

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class GenreCategoryID(
    @Column(name = "genre_id", nullable = false) val genreId: String,
    @Column(name = "category_id", nullable = false) val categoryId: String
) : Serializable {

    companion object {

        fun from(genreId: String, categoryId: String): GenreCategoryID {
            return GenreCategoryID(genreId, categoryId)
        }

    }

}