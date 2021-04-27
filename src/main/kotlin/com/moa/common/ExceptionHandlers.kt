package com.moa.common

import com.moa.exceptions.BadRequestException
import com.moa.exceptions.UnAuthorizedException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlers {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BadRequestException::class)
    fun badRequestErrorHandler(e: BadRequestException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.BAD_REQUEST.value(),
            data = ErrorResponse(e.message ?: "잘못된 요청입니다")
        )
        return ResponseEntity.badRequest().body(error)
    }

    @ExceptionHandler(RuntimeException::class)
    fun internalServerErrorHandler(e: RuntimeException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            data = ErrorResponse(e.message ?: "서버에 문제가 발생했습니다")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }

    @ExceptionHandler(AuthenticationException::class, UnAuthorizedException::class)
    fun unAuthorizedErrorHandler(e: RuntimeException): ResponseEntity<ApiResponse> {
        val error = ApiResponse(
            result = ResultType.FAIL,
            statusCode = HttpStatus.UNAUTHORIZED.value(),
            data = ErrorResponse(e.message ?: "인증 정보를 불러올 수 없습니다.")
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
    }
}
