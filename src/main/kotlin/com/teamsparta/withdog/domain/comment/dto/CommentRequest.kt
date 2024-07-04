package com.teamsparta.withdog.domain.comment.dto

import com.teamsparta.withdog.domain.comment.model.Comment
import com.teamsparta.withdog.domain.post.model.Post
import com.teamsparta.withdog.domain.user.model.User
import jakarta.validation.constraints.NotBlank

data class CommentRequest(
    @field:NotBlank
    val content: String
)
{
    fun toEntity(
        user: User,
        post: Post
    ): Comment
    {
        return Comment(
            content = this.content,
            user = user,
            post = post
        )
    }
}
