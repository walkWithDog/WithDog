package com.teamsparta.withdog.domain.user.repository

import com.teamsparta.withdog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {}