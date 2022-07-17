package com.fullcycle.admin.catalogo.infrastructure.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(value = ["com.fullcycle.admin.catalogo"])
class WebServerConfig