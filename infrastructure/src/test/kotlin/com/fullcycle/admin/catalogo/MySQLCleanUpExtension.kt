package com.fullcycle.admin.catalogo

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit.jupiter.SpringExtension

class MySQLCleanUpExtension: BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        val repositories = SpringExtension.getApplicationContext(context!!)
            .getBeansOfType(CrudRepository::class.java)
            .values

        repositories.forEach { it.deleteAll() }
    }
}