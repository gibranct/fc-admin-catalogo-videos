package com.fullcycle.admin.catalogo.domain.exceptions

import com.fullcycle.admin.catalogo.domain.AggregateRoot
import com.fullcycle.admin.catalogo.domain.Identifier
import com.fullcycle.admin.catalogo.domain.validation.Error
import kotlin.reflect.KClass

class NotFoundException(
    override val message: String,
    override val errors: List<Error>
) : DomainException(message, errors) {

    companion object {
        fun with(
            anAggregate: KClass<out AggregateRoot<*>>,
            anIdentifier: Identifier
        ): NotFoundException {
            val anError = "${anAggregate.simpleName} with id ${anIdentifier.value} not found"
            return NotFoundException(anError, listOf(Error(anError)))
        }

        fun with(error: Error): NotFoundException {
            return NotFoundException(error.message, listOf(error))
        }
    }
}