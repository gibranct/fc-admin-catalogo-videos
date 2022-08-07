package com.fullcycle.admin.catalogo.domain.category

import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.Validator

private const val NAME_MAX_LENGTH = 255

private const val NAME_MIN_LENGTH = 3

class CategoryValidator(
    private val category: Category,
    private val validationHandler: ValidationHandler
): Validator(validationHandler) {
    override fun validate() {
        val name = category.name.trim()
        if (name.isEmpty()) { validationHandler.validate(Error("'name' should not be null or empty")) }
        if (name.length < NAME_MIN_LENGTH || name.length > NAME_MAX_LENGTH) { validationHandler.validate(Error("'name' must be between 3 and 255 characters")) }
    }
}