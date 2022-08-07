package com.fullcycle.admin.catalogo

import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension

class MySQLCleanUpExtension: BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        val appContext = SpringExtension.getApplicationContext(context!!)

        val repositories = listOf(
            appContext.getBean(GenreRepository::class.java),
            appContext.getBean(CategoryRepository::class.java)
        )

        repositories.forEach {
            it.deleteAll()
            it.flush()
        }
    }
}