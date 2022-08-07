package com.fullcycle.admin.catalogo.application.genre.retrieve.list

import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.Genre
import java.time.Instant

data class ListGenresOutput internal constructor(
    val id: String,
    var name: String,
    var active: Boolean,
    val categoriesIds: MutableList<CategoryID>,
    val createdAt: Instant,
) {

    companion object {

        fun from(genre: Genre): ListGenresOutput {
            return ListGenresOutput(
                id = genre.id.value,
                name = genre.name,
                active = genre.active,
                categoriesIds = genre.categoriesIds(),
                createdAt = genre.createdAt,
            )
        }

    }

}