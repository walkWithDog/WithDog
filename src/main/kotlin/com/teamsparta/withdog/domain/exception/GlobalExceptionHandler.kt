package com.teamsparta.withdog.domain.exception

import com.teamsparta.withdog.domain.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler
{
    @ExceptionHandler(UsernameDuplicateException::class)
    fun handleUsernameDuplicateException(ex: UsernameDuplicateException): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(UsernameInvalidException::class)
    fun handleUsernameInvalidException(ex: UsernameInvalidException): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(PasswordLengthException::class)
    fun handlePasswordLengthException(ex: PasswordLengthException): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(PasswordContainsUsernameException::class)
    fun handlePasswordContainsUsernameException(ex: PasswordContainsUsernameException): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(LoginValidationException::class)
    fun handleLoginValidationException(ex: LoginValidationException): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(message = ex.message))
    }
}