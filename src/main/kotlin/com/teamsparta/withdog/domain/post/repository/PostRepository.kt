package com.teamsparta.withdog.domain.post.repository

import com.teamsparta.withdog.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>, CustomPostRepository {
}