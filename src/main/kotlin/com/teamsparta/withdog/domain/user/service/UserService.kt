package com.teamsparta.withdog.domain.user.service

import com.teamsparta.withdog.domain.exception.*
import com.teamsparta.withdog.domain.user.dto.*
import com.teamsparta.withdog.domain.user.model.User
import com.teamsparta.withdog.domain.user.model.UserProfile
import com.teamsparta.withdog.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
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
        val (username, password, nickname) = userSignUpRequest

        validateUsername(username)
        validatePassword(password, username)
        validateNickname(nickname)

        if (userRepository.existsByUsername(userSignUpRequest.username))
        {
            throw UsernameDuplicateException()
        }

       val user = User(
           username = userSignUpRequest.username,
           password = userSignUpRequest.password,
           profile = UserProfile(userSignUpRequest.username)
       )

        val savedUser = userRepository.save(user)

        return UserResponse.from(savedUser)
    }


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

    @Transactional
    fun updateProfile(
        userUpdateProfileRequest: UserUpdateProfileRequest, userId : Long
    ): UserResponse
    {

        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        if (userUpdateProfileRequest.password != userUpdateProfileRequest.passwordConfirmation) throw PasswordInvalidException()

        val password = userUpdateProfileRequest.password
        user.updateProfile(userUpdateProfileRequest, password)

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

private fun validateNickname(
    nickname: String
) {
    if (!Pattern.matches(
        "^[가-힣a-zA-Z0-9]{2,8}$",
        nickname
    )
        )
    {
        throw NicknameInvalidException()
    }
}