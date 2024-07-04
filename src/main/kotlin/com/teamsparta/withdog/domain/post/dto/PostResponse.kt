package com.teamsparta.withdog.domain.post.dto

import com.teamsparta.withdog.domain.comment.dto.CommentResponse
import com.teamsparta.withdog.domain.comment.model.Comment
import com.teamsparta.withdog.domain.post.model.Post
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val breedName: String,
    val content: String,
    val nickname: String,
    val imageUrl: String?,
    val likes: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val comments: List<CommentResponse>?
)
{
    companion object{
        fun from(
            post: Post,
            comment: List<CommentResponse>?
        ): PostResponse
        {
            return PostResponse(
                post.id ?: throw IllegalStateException("ID cannot be Null"),
                post.title,
                post.breedName,
                post.content,
                post.user.nickname,
                post.imageUrl,
                post.likes.size,
                post.createdAt,
                post.updatedAt,
                comment
            )
        }
    }
}
