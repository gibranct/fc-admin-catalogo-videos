package com.fullcycle.admin.catalogo.domain.pagination

data class SearchQuery(
    val page: Int,
    val perPage: Int,
    val term: String,
    val sort: String,
    val direction: String
)
