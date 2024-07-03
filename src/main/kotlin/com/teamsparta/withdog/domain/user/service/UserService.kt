package com.teamsparta.withdog.domain.user.service

import com.teamsparta.withdog.domain.user.dto.UserLogInRequest
import com.teamsparta.withdog.domain.user.dto.UserResponse
import com.teamsparta.withdog.domain.user.dto.UserSignUpRequest
import com.teamsparta.withdog.domain.user.dto.toEntity
import com.teamsparta.withdog.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(
    private val userRepository: UserRepository
) {

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