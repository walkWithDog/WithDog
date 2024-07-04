package com.teamsparta.withdog.domain.post.dto

import com.teamsparta.withdog.domain.post.model.Post
import java.io.Serializable

data class PopularPostResponse(
    val id: Long,
    val title: String,
) : Serializable
{
    companion object {
        fun from(
            post: Post
        ): PopularPostResponse {
            return PopularPostResponse(
                post.id ?: throw IllegalStateException("ID cannot be Null"),
                post.title,
            )
        }
    }
}