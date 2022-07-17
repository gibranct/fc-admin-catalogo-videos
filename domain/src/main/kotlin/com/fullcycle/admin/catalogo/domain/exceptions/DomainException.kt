package com.fullcycle.admin.catalogo.domain.exceptions

import com.fullcycle.admin.catalogo.domain.validation.Error

open class DomainException protected constructor(
    override val message: String,
    open val errors: List<Error>
): NoStacktraceException(message) {

    companion object {
        fun with(error: Error): DomainException {
            return DomainException(error.message, listOf(error))
        }

        fun with(errors: List<Error>): DomainException {
            return DomainException("", errors)
        }
    }
}