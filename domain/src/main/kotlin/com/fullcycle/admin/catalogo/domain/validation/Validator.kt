package com.fullcycle.admin.catalogo.domain.validation

abstract class Validator(
    val handler: ValidationHandler
) {

    abstract fun validate()

    protected fun validationHandler(): ValidationHandler {
        return this.handler
    }
}