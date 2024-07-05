package com.teamsparta.withdog.domain.user.dto

import jakarta.validation.constraints.NotBlank

data class UserLogInRequest(
    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String
)
