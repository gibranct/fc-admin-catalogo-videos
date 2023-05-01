package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

class QueueProperties(
    var exchange: String? = "",
    var routingKey: String? = "",
    var queue: String? = "",
) : InitializingBean {

    private val logger = LoggerFactory.getLogger(QueueProperties::class.java)


    override fun afterPropertiesSet() {
        logger.info(toString())
    }

    override fun toString(): String {
        return "QueueProperties(exchange=$exchange, routingKey=$routingKey, queue=$queue, logger=$logger)"
    }


}