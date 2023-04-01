package com.fullcycle.admin.catalogo.infrastructure.configuration

import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google.GoogleCloudProperties
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google.GoogleStorageProperties
import com.google.api.gax.retrying.RetrySettings
import com.google.auth.Credentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.http.HttpTransportOptions
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.threeten.bp.Duration
import java.io.ByteArrayInputStream
import java.util.*


@Configuration
@Profile(value =["development", "production"])
class GoogleCloudConfig {

    @Bean
    @ConfigurationProperties("google.cloud")
    fun googleCloudProperties(): GoogleCloudProperties {
        return GoogleCloudProperties()
    }

    @Bean
    @ConfigurationProperties("google.cloud.storage.catalogo-videos")
    fun googleStorageProperties(): GoogleStorageProperties {
        return GoogleStorageProperties()
    }

    @Bean
    fun credentials(props: GoogleCloudProperties): Credentials {
        val bytes = Base64.getDecoder().decode(props.credentials)
        return GoogleCredentials.fromStream(ByteArrayInputStream(bytes))
    }

    @Bean
    fun storage(
        credentials: Credentials,
        cloudConfig: GoogleCloudProperties,
        storageConfig: GoogleStorageProperties
    ): Storage {
        val transportOptions = HttpTransportOptions.newBuilder()
            .setConnectTimeout(storageConfig.connectTimeout)
            .setReadTimeout(storageConfig.readTimeout)
            .build()
        val retry = RetrySettings.newBuilder()
            .setInitialRetryDelay(Duration.ofMillis(storageConfig.retryDelay))
            .setMaxRetryDelay(Duration.ofMillis(storageConfig.retryMaxDelay))
            .setMaxAttempts(storageConfig.retryMaxAttempts)
            .setRetryDelayMultiplier(storageConfig.retryMultiplier)
            .build()
        return StorageOptions.newBuilder()
            .setCredentials(credentials)
            .setProjectId(cloudConfig.projectId)
            .setTransportOptions(transportOptions)
            .setRetrySettings(retry)
            .build()
            .service
    }
}