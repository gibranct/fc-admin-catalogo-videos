package com.fullcycle.admin.catalogo.infrastructure.api.controllers

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalogo.domain.validation.Error
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [DomainException::class])
    fun handleDomainException(ex: DomainException): ResponseEntity<*> {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex))
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex))
    }

    data class ApiError internal constructor(val message: String, val errors: List<Error>) {
        companion object {
            fun from(ex: DomainException): ApiError {
                return ApiError(ex.message, ex.errors)
            }
        }
    }

}