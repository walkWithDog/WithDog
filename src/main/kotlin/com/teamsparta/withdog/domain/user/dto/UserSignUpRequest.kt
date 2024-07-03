package com.teamsparta.withdog.domain.user.dto

import com.teamsparta.withdog.domain.user.model.User

data class UserSignUpRequest(
    val username: String,
    val password: String,
    val nickname: String
)

fun UserSignUpRequest.toEntity(): User
{
    return User(
        username = this.username,
        password = this.password,
        nickname = this.nickname
    )
}