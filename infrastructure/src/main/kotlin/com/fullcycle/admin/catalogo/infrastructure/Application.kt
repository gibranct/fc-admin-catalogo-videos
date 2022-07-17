package com.fullcycle.admin.catalogo.infrastructure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.AbstractEnvironment

@SpringBootApplication(scanBasePackages = ["com.fullcycle.admin.catalogo"])
class Application

fun main(args: Array<String>) {
    System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development")
    runApplication<Application>(*args)
}

