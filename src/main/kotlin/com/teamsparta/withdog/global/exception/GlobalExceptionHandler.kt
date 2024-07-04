package com.teamsparta.withdog.global.exception

import org.springframework.web.bind.annotation.RestControllerAdvice
import com.teamsparta.withdog.global.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException

@RestControllerAdvice
class GlobalExceptionHandler
{
    @ExceptionHandler(UsernameDuplicateException::class)
    fun handleUsernameDuplicateException(
        ex: UsernameDuplicateException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(UsernameInvalidException::class)
    fun handleUsernameInvalidException(
        ex: UsernameInvalidException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(PasswordLengthException::class)
    fun handlePasswordLengthException(
        ex: PasswordLengthException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(PasswordContainsUsernameException::class)
    fun handlePasswordContainsUsernameException(
        ex: PasswordContainsUsernameException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(LoginValidationException::class)
    fun handleLoginValidationException(
        ex: LoginValidationException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(message = ex.message))
    }

    //validation error handler
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException
    ): ResponseEntity<MutableMap<String, Any>>
    {
        val body: MutableMap<String, Any> = mutableMapOf("statusCode" to "400")
        val errors: MutableMap<String, String> = mutableMapOf()

        e.bindingResult.fieldErrors.forEach { fieldError ->
            errors[fieldError.field] = fieldError.defaultMessage ?: ""
        }

        body["errors"] = errors

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        e: IllegalStateException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(message = e.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message = e.message))
    }

    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(
        e: ModelNotFoundException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(message = e.message))
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(
        e: MaxUploadSizeExceededException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message = e.message))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(
        e: UnauthorizedException
    ): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(message = e.message))
    }

    @ExceptionHandler(PasswordInvalidException::class)
    fun handlePasswordInvalidException(ex: PasswordInvalidException): ResponseEntity<ErrorResponse>
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(message = ex.message))
    }
}



