package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean


class GoogleStorageProperties(
    var bucket: String = "",
    var connectTimeout: Int = 0,
    var readTimeout: Int = 0,
    var retryDelay: Long = 0,
    var retryMaxDelay: Long = 0,
    var retryMaxAttempts: Int = 0,
    var retryMultiplier: Double = 0.0,
) : InitializingBean {

    companion object {

        private val logger = LoggerFactory.getLogger(GoogleStorageProperties::class.java)

    }

    override fun afterPropertiesSet() {
        logger.info(toString())
    }



    override fun toString(): String {
        return "GoogleStorageProperties(bucket=$bucket, connectTimeout=$connectTimeout, readTimeout=$readTimeout, retryDelay=$retryDelay, retryMaxDelay=$retryMaxDelay, retryMaxAttempts=$retryMaxAttempts, retryMultiplier=$retryMultiplier)"
    }


}