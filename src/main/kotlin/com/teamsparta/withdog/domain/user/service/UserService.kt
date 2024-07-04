package com.teamsparta.withdog.domain.user.service

import com.teamsparta.withdog.domain.user.dto.*
import com.teamsparta.withdog.domain.user.model.User
import com.teamsparta.withdog.domain.user.model.UserProfile
import com.teamsparta.withdog.domain.user.model.UserRole
import com.teamsparta.withdog.domain.user.repository.UserRepository
import com.teamsparta.withdog.global.exception.*
import com.teamsparta.withdog.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
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
           password = passwordEncoder.encode(userSignUpRequest.password),
           profile = UserProfile(userSignUpRequest.username),
           role = UserRole.USER
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


        if (user.username != userLogInRequest.username ||
            !passwordEncoder.matches(userLogInRequest.password, user.password))

        {
            throw LoginValidationException()
        }

        val token = jwtPlugin.generateAccessToken(
            subject = user.id.toString(),
            username = user.username,
            role = UserRole.USER.toString()

        )

        return UserResponse.from(user, token)
    }

    @Transactional
    fun updateProfile(
        userUpdateProfileRequest: UserUpdateProfileRequest, userId : Long
    ): UserResponse
    {

        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User")
        if (userUpdateProfileRequest.password != userUpdateProfileRequest.passwordConfirmation) throw PasswordInvalidException()

        val password = userUpdateProfileRequest.password
        user.updateProfile(userUpdateProfileRequest, password)

        return UserResponse.from(user)

    }

    fun getProfileById(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User")

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