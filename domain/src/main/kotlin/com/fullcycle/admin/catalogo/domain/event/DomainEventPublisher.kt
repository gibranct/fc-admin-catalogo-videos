package com.fullcycle.admin.catalogo.domain.event

@FunctionalInterface
fun interface DomainEventPublisher {

    fun publishedEvent(event: DomainEvent)

}