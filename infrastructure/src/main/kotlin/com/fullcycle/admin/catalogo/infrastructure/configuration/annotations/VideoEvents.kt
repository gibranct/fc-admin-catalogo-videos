package com.fullcycle.admin.catalogo.infrastructure.configuration.annotations

import org.springframework.beans.factory.annotation.Qualifier

@Qualifier("VideoEvents")
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class VideoEvents()
