package com.teamsparta.withdog.domain.user.dto

data class UserSignUpRequest(
    val username: String,
    val password: String,
    val nickname: String
)
