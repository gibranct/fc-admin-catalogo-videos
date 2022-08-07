package com.fullcycle.admin.catalogo.application.genre.retrieve.get

import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.Genre
import java.time.Instant

data class GenreOutput internal constructor(
    val id: String,
    var name: String,
    var active: Boolean,
    val categoriesIds: MutableList<CategoryID>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
) {

    companion object {

        fun from(genre: Genre): GenreOutput {
            return GenreOutput(
                id = genre.id.value,
                name = genre.name,
                active = genre.active,
                categoriesIds = genre.categoriesIds(),
                createdAt = genre.createdAt,
                updatedAt = genre.updatedAt,
                deletedAt = genre.deletedAt
            )
        }

    }

}