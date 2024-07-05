package com.teamsparta.withdog.domain.user.model

import com.teamsparta.withdog.domain.user.dto.UserUpdateProfileRequest
import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder


@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Embedded
    var profile: UserProfile,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val role: UserRole = UserRole.USER
)
{
    fun updateProfile(
        userUpdateProfileRequest: UserUpdateProfileRequest,
        passwordEncoder: PasswordEncoder
    )
    {
        password = passwordEncoder.encode(userUpdateProfileRequest.password)
        profile.nickname = userUpdateProfileRequest.nickname
    }
}

