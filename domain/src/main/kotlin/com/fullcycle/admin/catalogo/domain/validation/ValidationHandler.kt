package com.fullcycle.admin.catalogo.domain.validation

interface ValidationHandler {

    fun append(aError: Error): ValidationHandler

    fun append(validationHandler: ValidationHandler): ValidationHandler

    fun append(validation: Validation): ValidationHandler

    fun getErrors(): List<Error>?

    fun hasError(): Boolean {
        return !getErrors().isNullOrEmpty()
    }

    fun firstError(): Error? {
        return getErrors()?.firstOrNull()
    }

    interface Validation {
        fun validate()
    }

}
