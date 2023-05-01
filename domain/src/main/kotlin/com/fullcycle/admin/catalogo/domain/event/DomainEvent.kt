package com.fullcycle.admin.catalogo.domain.event

import java.io.Serializable
import java.time.Instant

interface DomainEvent : Serializable {

    fun ocorred(): Instant

}