package com.teamsparta.withdog.domain.comment.repository

import com.teamsparta.withdog.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
    fun findByPostIdAndIsDeletedFalse(postId: Long): List<Comment>
}