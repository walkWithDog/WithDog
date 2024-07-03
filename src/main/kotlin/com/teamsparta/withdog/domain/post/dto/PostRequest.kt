package com.teamsparta.withdog.domain.post.dto

import com.teamsparta.withdog.domain.post.model.Post
import com.teamsparta.withdog.domain.user.model.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class PostRequest(
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String,

    @field:NotBlank
    @field:Pattern(
        regexp = "^[가-힣]*$",
        message = "한글만 입력 가능합니다.")
    val breedName: String,
)

fun PostRequest.toEntity(
    user: User,
    imageUrl: String?
): Post
{
    return Post(
        title = this.title,
        content = this.content,
        breedName = this.breedName,
        imageUrl = imageUrl,
        user = user
    )
}