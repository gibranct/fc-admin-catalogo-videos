package com.fullcycle.admin.catalogo.domain.exceptions

open class InternalErrorException private constructor(
    override val message: String,
    private val throwable: Throwable,
): NoStacktraceException(message) {

    companion object {
        fun with(error: String, throwable: Throwable): InternalErrorException {
            return InternalErrorException(error, throwable)
        }
    }
}