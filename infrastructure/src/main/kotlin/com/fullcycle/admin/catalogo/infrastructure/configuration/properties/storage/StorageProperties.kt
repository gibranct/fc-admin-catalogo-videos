package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

class StorageProperties(
    var filenamePattern: String? = "",
    var locationPattern: String? = "",
) : InitializingBean {



    companion object {

        private val logger = LoggerFactory.getLogger(StorageProperties::class.java)

    }

    override fun afterPropertiesSet() {
        logger.info(toString())
    }

    override fun toString(): String {
        return "StorageProperties(filenamePattern=$filenamePattern, locationPattern=$locationPattern)"
    }
}