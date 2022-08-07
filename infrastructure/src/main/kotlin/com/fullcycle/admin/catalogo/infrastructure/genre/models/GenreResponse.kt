package com.fullcycle.admin.catalogo.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class GenreResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("categories_id") val categories: List<String>,
    @JsonProperty("is_active") val active: Boolean,
    @JsonProperty("created_at") val createdAt: Instant,
    @JsonProperty("updated_at") val updatedAt: Instant,
    @JsonProperty("deleted_at") val deletedAt: Instant?,
)