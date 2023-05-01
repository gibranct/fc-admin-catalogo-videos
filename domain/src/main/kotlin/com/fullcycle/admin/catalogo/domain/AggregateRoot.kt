package com.fullcycle.admin.catalogo.domain

import com.fullcycle.admin.catalogo.domain.event.DomainEvent

abstract class AggregateRoot<ID: Identifier>(
    override val id: ID,
    private val domainEvents: List<DomainEvent>? = emptyList(),
) : Entity<ID>(id, domainEvents?.toMutableList()) {
}