package com.fullcycle.admin.catalogo.domain.utils

import java.time.Instant
import java.time.temporal.ChronoUnit


final class InstantUtils {

    companion object {

        fun now(): Instant {
            return Instant.now().truncatedTo(ChronoUnit.MICROS)
        }

    }

}