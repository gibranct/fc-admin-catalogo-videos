package com.fullcycle.admin.catalogo.infrastructure

import com.fullcycle.admin.catalogo.MySQLCleanUpExtension
import com.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = [WebServerConfig::class])
@ExtendWith(MySQLCleanUpExtension::class)
annotation class IntegrationTest()