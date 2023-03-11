package com.fullcycle.admin.catalogo.domain.validation.handler

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.validation.Error
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler

class ThrowsValidationHandler: ValidationHandler {
    override fun append(aError: Error): ValidationHandler {
        throw DomainException.with(aError)
    }

    override fun append(anHandler: ValidationHandler): ValidationHandler {
        throw DomainException.with(anHandler.getErrors())
    }

    override fun validate(validationHandler: ValidationHandler): ValidationHandler {
        throw validationHandler.getErrors().let { DomainException.with(it) }
    }

    override fun <T> validate(validation: ValidationHandler.Validation<T>): T? {
        try {
           return validation.validate()
        } catch (ex: Exception) {
            throw DomainException.with(Error(ex.localizedMessage))
        }
    }

    override fun getErrors(): List<Error> {
        return listOf()
    }
}