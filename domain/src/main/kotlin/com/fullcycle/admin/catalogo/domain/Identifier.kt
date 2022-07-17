package com.fullcycle.admin.catalogo.domain

abstract class Identifier(
    open val value: String
): ValueObject() {
}
