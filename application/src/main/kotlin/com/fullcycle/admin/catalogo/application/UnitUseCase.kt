package com.fullcycle.admin.catalogo.application

open abstract class UnitUseCase<IN> {

    abstract fun execute(anIn: IN)

}