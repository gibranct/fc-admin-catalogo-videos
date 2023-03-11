package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID
import com.fullcycle.admin.catalogo.domain.category.CategoryID
import com.fullcycle.admin.catalogo.domain.genre.GenreID

data class VideoSearchQuery(
    val page: Int,
    val perPage: Int,
    val terms: String,
    val sort: String,
    val direction: String,
    val castMembers: Set<CastMemberID>,
    val categories: Set<CategoryID>,
    val genres: Set<GenreID>,
)
