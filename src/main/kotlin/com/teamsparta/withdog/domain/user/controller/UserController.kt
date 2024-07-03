package com.teamsparta.withdog.domain.user.controller

import com.teamsparta.withdog.domain.user.dto.*
import com.teamsparta.withdog.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun signUp(
        @RequestBody userSignUpRequest: UserSignUpRequest
    ): ResponseEntity<UserResponse>
    {
        userService.signUp(userSignUpRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun logIn(
        @RequestBody userLogInRequest: UserLogInRequest
    ): ResponseEntity<UserResponse>
    {
        userService.login(userLogInRequest)
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}