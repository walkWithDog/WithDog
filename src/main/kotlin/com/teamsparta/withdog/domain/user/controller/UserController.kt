package com.teamsparta.withdog.domain.user.controller

import com.teamsparta.withdog.domain.user.dto.UserLogInRequest
import com.teamsparta.withdog.domain.user.dto.UserResponse
import com.teamsparta.withdog.domain.user.dto.UserSignUpRequest
import com.teamsparta.withdog.domain.user.dto.UserUpdateProfileRequest
import com.teamsparta.withdog.domain.user.service.UserService
import com.teamsparta.withdog.infra.security.jwt.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
)
{
    @PostMapping("/signup")
    fun signUp(
        @Valid @RequestBody userSignUpRequest: UserSignUpRequest
    ): ResponseEntity<UserResponse>
    {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signUp(userSignUpRequest))
    }

    @PostMapping("/login")
    fun logIn(
        @Valid @RequestBody userLogInRequest: UserLogInRequest
    ): ResponseEntity<UserResponse>
    {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(userLogInRequest))
    }

    @PatchMapping("/users/profile")
    fun updateProfile(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody userUpdateProfileRequest: UserUpdateProfileRequest
    ): ResponseEntity<UserResponse>
    {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updateProfile(userUpdateProfileRequest, principal.id))
    }

    @GetMapping("/{userId}")
    fun getProfileById(
        @PathVariable userId: Long
    ): ResponseEntity<UserResponse>
    {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getProfileById(userId))
    }
}