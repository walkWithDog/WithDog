package com.teamsparta.withdog.domain.user.dto

import com.teamsparta.withdog.domain.user.model.UserRole

data class UserSignUpRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val role: UserRole = UserRole.USER
)
