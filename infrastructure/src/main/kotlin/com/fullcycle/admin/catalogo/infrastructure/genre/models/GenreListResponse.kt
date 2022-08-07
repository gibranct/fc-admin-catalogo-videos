package com.fullcycle.admin.catalogo.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class GenreListResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("is_active") val active: Boolean,
    @JsonProperty("created_at") val createdAt: Instant,
)