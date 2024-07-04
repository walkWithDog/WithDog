package com.teamsparta.withdog.domain.user.controller

import com.teamsparta.withdog.domain.user.dto.*
import com.teamsparta.withdog.domain.user.service.UserService
import com.teamsparta.withdog.infra.security.jwt.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signup")
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
        val response = userService.login(userLogInRequest)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/users/profile")
    fun updateProfile(
        @AuthenticationPrincipal principal: UserPrincipal,
        @RequestBody userUpdateProfileRequest: UserUpdateProfileRequest
    ): ResponseEntity<UserResponse>
    {
        userService.updateProfile(userUpdateProfileRequest, principal.id)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @GetMapping("/{userId}")
    fun getProfileById(
        @PathVariable userId : Long
    ): ResponseEntity<UserResponse>
    {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getProfileById(userId))

    }
}