package com.teamsparta.withdog.domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserProfile(
    @Column(name = "nickname", nullable = false)
    var nickname: String
)