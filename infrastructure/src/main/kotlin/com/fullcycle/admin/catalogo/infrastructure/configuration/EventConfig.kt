package com.fullcycle.admin.catalogo.infrastructure.configuration

import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import com.fullcycle.admin.catalogo.infrastructure.services.EventService
import com.fullcycle.admin.catalogo.infrastructure.services.impl.RabbitEventService
import org.springframework.amqp.rabbit.core.RabbitOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventConfig {

    @Bean
    @VideoCreatedQueue
    fun videoCreatedEventService(
        @VideoCreatedQueue props: QueueProperties,
        ops: RabbitOperations
    ): EventService {
        return RabbitEventService(
            exchange =  props.exchange!!,
            routingKey = props.routingKey!!,
            rabbitOperations =  ops
        )
    }

}