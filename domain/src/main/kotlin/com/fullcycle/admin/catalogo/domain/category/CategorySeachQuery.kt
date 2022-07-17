package com.fullcycle.admin.catalogo.domain.category

data class CategorySeachQuery(
    val page: Int,
    val perPage: Int,
    val term: String,
    val sort: String,
    val direction: String
) {

}
