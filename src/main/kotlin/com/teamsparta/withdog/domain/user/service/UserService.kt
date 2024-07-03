package com.teamsparta.withdog.domain.user.service

import com.teamsparta.withdog.domain.exception.*
import com.teamsparta.withdog.domain.user.dto.UserLogInRequest
import com.teamsparta.withdog.domain.user.dto.UserResponse
import com.teamsparta.withdog.domain.user.dto.UserSignUpRequest
import com.teamsparta.withdog.domain.user.dto.toEntity
import com.teamsparta.withdog.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern


@Service
class UserService(
    private val userRepository: UserRepository
)
{
    @Transactional
    fun signUp(userSignUpRequest: UserSignUpRequest): UserResponse {

        return UserResponse.from(userRepository.save(userSignUpRequest.toEntity(userSignUpRequest)))
    }

    @Transactional
    fun login(userLogInRequest: UserLogInRequest): UserResponse {
        val user = userRepository.findByUsername(userLogInRequest.username)
            ?: throw RuntimeException("User does not exists")

        return UserResponse.from(userRepository.save(user))
    }
}

private fun validateNickname(username: String) {

    if (!Pattern.matches(
            "^[a-zA-Z0-9]{3,10}$",
            username
        )
    ) {
        throw UsernameInvalidException()
    }
}

private fun validatePassword(password: String, username: String) {

    if (!Pattern.matches(
            "^.{4,16}$",
            password
        )
    ) {
        throw PasswordLengthException()
    }
    if (password.contains(username)) {

        throw PasswordContainsUsernameException()
    }
}