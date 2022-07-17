package com.fullcycle.admin.catalogo.domain

abstract class AggregateRoot<ID: Identifier>(override val id: ID) : Entity<ID>(id) {
}