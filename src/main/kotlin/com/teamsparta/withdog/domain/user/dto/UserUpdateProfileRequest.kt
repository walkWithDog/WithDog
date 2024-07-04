package com.teamsparta.withdog.domain.user.dto

data class UserUpdateProfileRequest (
    val password : String,
    val passwordConfirmation : String,
    var nickname : String
)