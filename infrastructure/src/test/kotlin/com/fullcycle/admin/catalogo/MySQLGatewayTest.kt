package com.fullcycle.admin.catalogo.infrastructure

import com.fullcycle.admin.catalogo.MySQLCleanUpExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@DataJpaTest
@ComponentScan(
    basePackages = ["com.fullcycle.admin.catalogo"], // TODO:
    includeFilters = [ComponentScan.Filter(type = FilterType.REGEX, pattern = [".[MySQLGateway]"])]
)
@ExtendWith(MySQLCleanUpExtension::class)
annotation class MySQLGatewayTest