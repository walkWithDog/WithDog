package com.teamsparta.withdog.domain.like.repository

import com.teamsparta.withdog.domain.like.model.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository: JpaRepository<Like, Long>
{
    fun findByPostId(postId: Long): Like?
    fun deleteAllByPostId(postId: Long)
}