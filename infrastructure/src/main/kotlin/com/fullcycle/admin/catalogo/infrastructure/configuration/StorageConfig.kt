package com.fullcycle.admin.catalogo.infrastructure.configuration

import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google.GoogleStorageProperties
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService
import com.fullcycle.admin.catalogo.infrastructure.services.impl.GCStorageService
import com.fullcycle.admin.catalogo.infrastructure.services.local.InMemoryStorageService
import com.google.cloud.storage.Storage
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class StorageConfig {

    @Bean
    @ConfigurationProperties(prefix = "storage.catalogo-videos")
    fun storageProperties() = StorageProperties()

    @Bean
    @Profile(value = ["production"])
    fun gcStorageAPI(
        props: GoogleStorageProperties,
        storage: Storage
    ): StorageService {
        return GCStorageService(props.bucket, storage)
    }


    @Bean
    @ConditionalOnMissingBean
    fun localStorageAPI(): StorageService {
        return InMemoryStorageService()
    }

}