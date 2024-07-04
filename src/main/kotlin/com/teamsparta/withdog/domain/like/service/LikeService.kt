package com.teamsparta.withdog.domain.like.service

import com.teamsparta.withdog.domain.like.model.Like
import com.teamsparta.withdog.domain.like.repository.LikeRepository
import com.teamsparta.withdog.domain.post.model.Post
import com.teamsparta.withdog.domain.user.repository.UserRepository
import com.teamsparta.withdog.global.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository
)
{
    fun updateLike(
        userId: Long,
        post: Post
    )
    {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ModelNotFoundException("없는 사용자 입니다.")

        val like = likeRepository.findByPostId(post.id!!)
        if (post.user.id == userId)
            throw IllegalArgumentException("본인이 작성한 글에는 남길 수 없습니다.")

        if (like == null) {
            likeRepository.save(
                Like(
                    user = user,
                    post = post
                )
            )
        } else likeRepository.delete(like)
    }

    fun deleteLike(
        post: Post
    )
    {
        likeRepository.deleteAllByPostId(post.id!!)
    }
}