package com.fullcycle.admin.catalogo.application

abstract class NullaryUseCase<OUT> {

    abstract fun execute(): OUT

}