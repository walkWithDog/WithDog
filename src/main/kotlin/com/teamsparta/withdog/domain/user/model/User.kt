package com.teamsparta.withdog.domain.user.model

import com.teamsparta.withdog.domain.user.dto.UserUpdateProfileRequest
import jakarta.persistence.*


@Entity
@Table(name = "users")
class User(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Embedded
    var profile: UserProfile,
)
{
    fun updateProfile(userUpdateProfileRequest: UserUpdateProfileRequest, password: String)
    {
        this.password = password
        this.profile.nickname = userUpdateProfileRequest.nickname
    }
}