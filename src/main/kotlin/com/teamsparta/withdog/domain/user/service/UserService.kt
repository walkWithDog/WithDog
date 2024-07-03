package com.teamsparta.withdog.domain.user.service

import com.teamsparta.withdog.domain.user.dto.*
import com.teamsparta.withdog.domain.user.repository.UserRepository
import com.teamsparta.withdog.global.exception.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun signUp(
        userSignUpRequest: UserSignUpRequest
    ): UserResponse
    {
        val (username, password) = userSignUpRequest

        validateUsername(username)
        validatePassword(password, username)

        if (userRepository.existsByUsername(userSignUpRequest.username))
        {
            throw UsernameDuplicateException()
        }

        return UserResponse.from(userRepository.save(userSignUpRequest.toEntity()))
    }

    @Transactional
    fun login(
        userLogInRequest: UserLogInRequest
    ): UserResponse
    {
        val user = userRepository.findByUsername(userLogInRequest.username)
            ?: throw LoginValidationException()

        // passwordEncoder 적용해야할 것!
        if (user.username != userLogInRequest.username ||
            user.password != userLogInRequest.password
        )
        {
            throw LoginValidationException()
        }

        return UserResponse.from(user)
    }
}

private fun validateUsername(
    username: String
) {

    if (!Pattern.matches(
            "^[a-zA-Z0-9]{3,10}$",
            username
        )
    )
    {
        throw UsernameInvalidException()
    }
}

private fun validatePassword(
    password: String, username: String
) {

    if (!Pattern.matches(
            "^.{4,16}$",
            password
        )
    )
    {
        throw PasswordLengthException()
    }
    if (password.contains(username))
    {
        throw PasswordContainsUsernameException()
    }
}