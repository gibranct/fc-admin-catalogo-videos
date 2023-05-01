package com.fullcycle.admin.catalogo.infrastructure.configuration.annotations

import org.springframework.beans.factory.annotation.Qualifier

@Qualifier("VideoCreated")
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class VideoCreatedQueue()
