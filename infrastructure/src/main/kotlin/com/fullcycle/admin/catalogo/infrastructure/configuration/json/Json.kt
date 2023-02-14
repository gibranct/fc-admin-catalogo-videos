package com.fullcycle.admin.catalogo.infrastructure.configuration.json

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

enum class Json {

    INSTANCE;

    companion object {

        fun mapper(): ObjectMapper {
            return INSTANCE.mapper.copy()
        }

        fun writeValueAsString(obj: Any): String {
            return INSTANCE.mapper.writeValueAsString(obj)
        }

        fun <T>readValue(json: String, clazz: Class<T>): T {
            return INSTANCE.mapper.readValue(json, clazz)
        }
    }

    val mapper = Jackson2ObjectMapperBuilder()
        .dateFormat(StdDateFormat())
        .featuresToDisable(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
            DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        )
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .build<ObjectMapper>()
}