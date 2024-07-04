package com.teamsparta.withdog.domain.user.dto

import com.teamsparta.withdog.domain.user.model.User

data class UserSignUpRequest(
    val username: String,
    val password: String,
    val nickname: String
)
