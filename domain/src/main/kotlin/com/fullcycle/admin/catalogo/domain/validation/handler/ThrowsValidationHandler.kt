package com.fullcycle.admin.catalogo.domain.validation.handler

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler

class ThrowsValidationHandler: ValidationHandler {
    override fun append(aError: Error): ValidationHandler {
        throw DomainException.with(aError)
    }

    override fun append(validationHandler: ValidationHandler): ValidationHandler {
        throw validationHandler.getErrors()?.let { DomainException.with(it) }!!
    }

    override fun append(validation: ValidationHandler.Validation): ValidationHandler {
        try {
            validation.validate()
        } catch (ex: Exception) {
            throw DomainException.with(Error(ex.localizedMessage))
        }

        return this
    }

    override fun getErrors(): List<Error>? {
        return listOf()
    }
}