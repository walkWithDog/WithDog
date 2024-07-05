package com.teamsparta.withdog.domain.user.dto

import com.teamsparta.withdog.domain.user.model.User
import com.teamsparta.withdog.domain.user.model.UserProfile
import com.teamsparta.withdog.domain.user.model.UserRole
import jakarta.validation.constraints.NotBlank
import org.springframework.security.crypto.password.PasswordEncoder

data class UserSignUpRequest(
    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val nickname: String,

    val role: UserRole = UserRole.USER
)

fun UserSignUpRequest.toEntity(
    passwordEncoder: PasswordEncoder
): User {
    return User(
        username = this.username,
        password = passwordEncoder.encode(this.password),
        profile = UserProfile(this.nickname),
        role = this.role
    )
}
