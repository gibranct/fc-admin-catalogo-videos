package com.fullcycle.admin.catalogo.domain.castmember

import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.Validator
import com.fullcycle.admin.catalogo.domain.validation.Error

data class CastMemberValidator(
    val castMember: CastMember,
    val validationHandler: ValidationHandler
): Validator(validationHandler) {

    companion object {
        private const val NAME_MAX_LENGTH = 255
        private const val NAME_MIN_LENGTH = 3
    }

    override fun validate() {
        this.checkNameConstraints()
    }

    private fun checkNameConstraints() {
        val name = this.castMember.name
        if (name.isBlank()) {
            validationHandler.append(Error("'name' should not be empty"))
            return
        }
        val length = name.trim().length
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            validationHandler().append(Error("'name' must be between 3 and 255 characters"))
        }
    }
}