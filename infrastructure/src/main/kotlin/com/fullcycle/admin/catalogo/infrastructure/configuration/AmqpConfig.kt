package com.fullcycle.admin.catalogo.infrastructure.configuration

import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEvents
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import org.springframework.amqp.core.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    @VideoCreatedQueue
    fun videoCreatedQueueProperties(): QueueProperties {
        return QueueProperties()
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    @VideoEncodedQueue
    fun videoEncodedQueueProperties(): QueueProperties? {
        return QueueProperties()
    }

    companion object {

        @Configuration
        class Admin {


            @Bean
            @VideoEvents
            fun videoEventsExchange(@VideoCreatedQueue props: QueueProperties): Exchange {
                return DirectExchange(props.exchange)
            }

            @Bean
            @VideoCreatedQueue
            fun videoCreatedQueue(@VideoCreatedQueue props: QueueProperties): Queue {
                return Queue(props.queue)
            }

            @Bean
            @VideoCreatedQueue
            fun videoCreatedQueueBinding(
                @VideoEvents exchange: DirectExchange?,
                @VideoCreatedQueue queue: Queue?,
                @VideoCreatedQueue props: QueueProperties
            ): Binding {
                return BindingBuilder.bind(queue).to(exchange).with(props.routingKey)
            }

            @Bean
            @VideoEncodedQueue
            fun videoEncodedQueue(@VideoEncodedQueue props: QueueProperties): Queue {
                return Queue(props.queue)
            }

            @Bean
            @VideoEncodedQueue
            fun videoEncodedQueueBinding(
                @VideoEvents exchange: DirectExchange,
                @VideoEncodedQueue queue: Queue,
                @VideoEncodedQueue props: QueueProperties
            ): Binding {
                return BindingBuilder.bind(queue).to(exchange).with(props.routingKey)
            }

        }

    }
}