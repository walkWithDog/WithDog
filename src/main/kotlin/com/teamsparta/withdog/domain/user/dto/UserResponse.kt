package com.teamsparta.withdog.domain.user.dto

import com.teamsparta.withdog.domain.user.model.User

data class UserResponse(
    val id: Long,
    val username: String,
    val nickname: String,
)
{
    var token : String? = null

    companion object
    {
        fun from(saveUser: User): UserResponse
        {
            return UserResponse(
                saveUser.id ?: throw IllegalStateException("ID cannot be Null"),
                saveUser.username,
                saveUser.profile.nickname
            )
        }

        fun from(saveUser: User, token : String): UserResponse
        {
            val userResponse = UserResponse(
                saveUser.id ?: throw IllegalStateException("ID cannot be Null"),
                saveUser.username,
                saveUser.profile.nickname
            )
            userResponse.token = token

            return userResponse
        }
    }
}