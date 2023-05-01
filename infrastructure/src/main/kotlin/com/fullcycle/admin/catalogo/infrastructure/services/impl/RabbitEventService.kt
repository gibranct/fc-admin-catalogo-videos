package com.fullcycle.admin.catalogo.infrastructure.services.impl

import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json
import com.fullcycle.admin.catalogo.infrastructure.services.EventService
import org.springframework.amqp.rabbit.core.RabbitOperations

class RabbitEventService(
    private val exchange: String,
    private val routingKey: String,
    private val rabbitOperations: RabbitOperations,
) : EventService {


    override fun send(event: Any) {
        this.rabbitOperations.convertAndSend(exchange, routingKey, Json.writeValueAsString(event))
    }


}