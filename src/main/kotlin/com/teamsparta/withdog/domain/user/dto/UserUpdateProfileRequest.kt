package com.teamsparta.withdog.domain.user.dto

import jakarta.validation.constraints.NotBlank

data class UserUpdateProfileRequest(
    val password: String,
    val passwordConfirmation: String,

    @field:NotBlank
    var nickname: String
)