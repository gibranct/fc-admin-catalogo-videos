package com.fullcycle.admin.catalogo.infrastructure.category.models

data class CreateCategoryRequest(
   val name: String,
   val description: String?,
   val isActive: Boolean
)