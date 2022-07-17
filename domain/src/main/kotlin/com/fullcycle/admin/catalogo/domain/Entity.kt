package com.fullcycle.admin.catalogo.domain

import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler

abstract class Entity<ID: Identifier>(
    open val id: ID,
) {

    abstract fun validate(handler: ValidationHandler)

}