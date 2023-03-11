package com.fullcycle.admin.catalogo.domain.video

import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.Validator

private const val TITLE_MAX_LENGTH = 255
private const val DESCRIPTION_MAX_LENGTH = 4_000

data class VideoValidator(
    val video: Video,
    val validationHandler: ValidationHandler,
) : Validator(validationHandler) {



    override fun validate() {
        val title = video.title
        val description = video.description
        if (title.isEmpty()) { validationHandler.append(Error("'title' should not be null or empty")) }
        if (description.isEmpty()) { validationHandler.append(Error("'description' should not be null or empty")) }
        if (title.length > TITLE_MAX_LENGTH) { validationHandler.append(Error("'name' must be between 1 and 255 characters")) }
        if (description.length > DESCRIPTION_MAX_LENGTH) { validationHandler.append(Error("'description' must be between 1 and 4000 characters")) }
    }

}
