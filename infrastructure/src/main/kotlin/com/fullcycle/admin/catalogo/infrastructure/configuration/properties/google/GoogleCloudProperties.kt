package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

class GoogleCloudProperties(
    var credentials: String? = "",
    var projectId: String? = "",
) : InitializingBean {


    companion object {

        private val logger = LoggerFactory.getLogger(GoogleCloudProperties::class.java)

    }

    override fun afterPropertiesSet() {
        logger.info(toString())
    }

    override fun toString(): String {
        return "GoogleCloudProperties(credentials=$credentials, projectId=$projectId)"
    }


}