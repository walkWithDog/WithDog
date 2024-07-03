package com.teamsparta.withdog.domain.user.dto

import com.teamsparta.withdog.domain.user.model.User

data class UserResponse(
    val id: Long?,
    val username: String,
    val nickname: String
) {
    companion object
    {
        fun from(saveUser: User): UserResponse
        {

            return UserResponse(
                saveUser.id,
                saveUser.username,
                saveUser.nickname
            )

        }
    }
}