package com.fullcycle.admin.catalogo.domain

import com.fullcycle.admin.catalogo.domain.event.DomainEvent
import com.fullcycle.admin.catalogo.domain.event.DomainEventPublisher
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler

abstract class Entity<ID: Identifier>(
    open val id: ID,
    private var domainEvents: MutableList<DomainEvent>? = mutableListOf(),
) {

    fun getDomainEvents() = this.domainEvents!!.toList()

    fun publishDomainEvents(publisher: DomainEventPublisher) {
        domainEvents?.forEach(publisher::publishedEvent)
        domainEvents?.clear()
    }

    fun registerEvent(event: DomainEvent) {
        domainEvents?.add(event)
    }

    abstract fun validate(handler: ValidationHandler)
}