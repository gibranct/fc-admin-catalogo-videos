package com.fullcycle.admin.catalogo.domain.validation

interface ValidationHandler {

    fun append(aError: Error): ValidationHandler

    fun append(anHandler: ValidationHandler): ValidationHandler

    fun validate(validationHandler: ValidationHandler): ValidationHandler

     fun <T> validate(validation: Validation<T>): T?

    fun getErrors(): List<Error>

    fun hasError(): Boolean {
        return getErrors().isNotEmpty()
    }

    fun firstError(): Error? {
        return getErrors().firstOrNull()
    }

    fun interface Validation<T> {
        fun validate(): T
    }

}
