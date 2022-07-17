package com.fullcycle.admin.catalogo.domain.validation.handler

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler
import com.fullcycle.admin.catalogo.domain.validation.Error

class Notification private constructor(
    private val errors: MutableList<Error>
): ValidationHandler {
    override fun append(aError: Error): Notification {
        errors.add(aError)
        return this
    }

    override fun append(validationHandler: ValidationHandler): Notification {
        validationHandler.getErrors()?.let { errors.addAll(it) }
        return this
    }

    override fun append(validation: ValidationHandler.Validation): Notification {
        try {
            validation.validate()
        } catch (ex: DomainException) {
            errors.addAll(ex.errors)
        } catch (th: Throwable) {
            errors.add(Error(th.message ?: ""))
        }
        return this
    }

    override fun getErrors(): List<Error> {
        return errors
    }

    companion object {
        fun create(): Notification {
            return Notification(mutableListOf())
        }

        fun create(anError: Error): Notification {
            return Notification(mutableListOf()).append(anError)
        }

        fun create(aThrowable: Throwable): Notification {
            return Notification(mutableListOf()).append(Error(aThrowable.message ?: ""))
        }
    }
}