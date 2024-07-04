package com.teamsparta.withdog.domain.comment.dto

import com.teamsparta.withdog.domain.comment.model.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val content: String,
    val nickname: String,
    val createAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)
{
    companion object {
        fun from(
            comment: Comment
        ): CommentResponse
        {
            return CommentResponse(
                id = comment.id ?: throw IllegalStateException("Comment Id cannot be null"),
                content = comment.content,
                nickname = comment.user.profile.nickname,
                createAt = comment.createdAt,
                updatedAt = comment.updatedAt,
            )
        }
    }
}
