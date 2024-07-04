package com.teamsparta.withdog.domain.user.model

import jakarta.persistence.*


@Entity
@Table(name = "users")
class User(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,
)