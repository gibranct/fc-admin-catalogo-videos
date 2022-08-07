package com.fullcycle.admin.catalogo.application

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
abstract class UseCaseTest: BeforeEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        Mockito.reset(getMocks())
    }

    abstract fun getMocks(): List<Any>

}