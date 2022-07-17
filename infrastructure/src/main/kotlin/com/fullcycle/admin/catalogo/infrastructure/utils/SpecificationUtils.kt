package com.fullcycle.admin.catalogo.infrastructure.utils

import org.springframework.data.jpa.domain.Specification

sealed class SpecificationUtils private constructor() {

    companion object {

        fun <T>like(
            prop: String,
            term: String,
        ): Specification<T> {
            return Specification<T> { root, _, cb -> cb.like(cb.upper(root.get(prop)), "%${term.uppercase()}%") }
        }
    }

}