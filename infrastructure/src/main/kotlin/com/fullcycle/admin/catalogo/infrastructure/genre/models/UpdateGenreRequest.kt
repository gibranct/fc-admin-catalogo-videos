package com.fullcycle.admin.catalogo.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateGenreRequest(
    @JsonProperty("name") val name: String,
    @JsonProperty("categories_id") val categories: List<String>?,
    @JsonProperty("is_active") val active: Boolean,
)