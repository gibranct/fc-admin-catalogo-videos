package com.fullcycle.admin.catalogo.domain.genre


import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.Validator

private const val NAME_MAX_LENGTH = 255

private const val NAME_MIN_LENGTH = 1

class GenreValidator(
    private val genre: Genre,
    private val validationHandler: ValidationHandler
): Validator(validationHandler) {

    override fun validate() {
        val name = genre.name.trim()
        if (name.isEmpty()) { validationHandler.append(Error("'name' should not be null or empty")) }
        if (name.length < NAME_MIN_LENGTH || name.length > NAME_MAX_LENGTH) { validationHandler.append(Error("'name' must be between 1 and 255 characters")) }
    }

}