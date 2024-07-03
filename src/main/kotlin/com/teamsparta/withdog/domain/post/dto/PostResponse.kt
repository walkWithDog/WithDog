package com.teamsparta.withdog.domain.post.dto

import com.teamsparta.withdog.domain.post.model.Post
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val breedName: String,
    val content: String,
    val nickname: String,
    val imageUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val isDeleted: Boolean
)
{
    companion object{
        fun from(
            post: Post
        ): PostResponse
        {
            return PostResponse(
                post.id ?: throw IllegalStateException("ID cannot be Null"),
                post.title,
                post.breedName,
                post.content,
                post.user.nickname,
                post.imageUrl,
                post.createdAt,
                post.updatedAt,
                post.isDeleted
            )
        }
    }
}
