package com.teamsparta.withdog.domain.post.dto

import com.teamsparta.withdog.domain.post.model.Post

data class PopularPostResponse(
    val id: Long,
    val title: String,
)
{
    companion object{
        fun from(
            post: Post
        ): PopularPostResponse
        {
            return PopularPostResponse(
                post.id ?: throw IllegalStateException("ID cannot be Null"),
                post.title,
            )
        }
    }
}