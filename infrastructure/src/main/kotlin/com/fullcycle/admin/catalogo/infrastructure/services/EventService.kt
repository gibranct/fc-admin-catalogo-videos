package com.fullcycle.admin.catalogo.infrastructure.services

interface EventService {

    fun send(event: Any)

}