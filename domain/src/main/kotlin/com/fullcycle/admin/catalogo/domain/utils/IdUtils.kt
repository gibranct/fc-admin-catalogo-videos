package com.fullcycle.admin.catalogo.domain.utils

import java.util.*


class IdUtils {

    companion object {
        fun uuid(): String {
            return UUID.randomUUID().toString().lowercase(Locale.getDefault()).replace("-", "")
        }
    }

}