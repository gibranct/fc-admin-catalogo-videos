package com.fullcycle.admin.catalogo.domain.exceptions

import com.fullcycle.admin.catalogo.domain.validation.handler.Notification

data class NotificationException(
    private val aMessage: String,
    val notification: Notification
) : DomainException(aMessage, notification.getErrors())
