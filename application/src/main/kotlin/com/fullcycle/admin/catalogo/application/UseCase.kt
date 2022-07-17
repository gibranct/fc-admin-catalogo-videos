package com.fullcycle.admin.catalogo.application

open abstract class UseCase<IN, OUT> {

    abstract fun execute(anIn: IN): OUT

}