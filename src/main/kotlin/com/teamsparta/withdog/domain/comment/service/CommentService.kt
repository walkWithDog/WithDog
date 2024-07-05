package com.teamsparta.withdog.domain.comment.service

import com.teamsparta.withdog.domain.comment.dto.CommentRequest
import com.teamsparta.withdog.domain.comment.dto.CommentResponse
import com.teamsparta.withdog.domain.comment.dto.toEntity
import com.teamsparta.withdog.domain.comment.repository.CommentRepository
import com.teamsparta.withdog.domain.post.repository.PostRepository
import com.teamsparta.withdog.domain.user.repository.UserRepository
import com.teamsparta.withdog.global.exception.ModelNotFoundException
import com.teamsparta.withdog.global.exception.UnauthorizedException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
)
{
    fun getCommentList(
        postId: Long
    ): List<CommentResponse>
    {
        val comment = commentRepository.findByPostIdAndIsDeletedFalse(postId)
        return comment.map { CommentResponse.from(it) }
    }

    @Transactional
    fun createComment(
        postId: Long,
        userId: Long,
        commentRequest: CommentRequest
    ): CommentResponse
    {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("삭제된 게시글입니다.")
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ModelNotFoundException("없는 사용자 입니다.")

        return CommentResponse.from(commentRepository.save(commentRequest.toEntity(user, post)))
    }

    @Transactional
    fun updateComment(
        postId: Long,
        userId: Long,
        commentId: Long,
        commentRequest: CommentRequest
    ): CommentResponse
    {
        postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("삭제된 게시글입니다.")
        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw ModelNotFoundException("삭제된 댓글입니다.")

        if (comment.isDeleted)
            throw ModelNotFoundException("삭제된 댓글입니다")

        if(userId != comment.user.id )
            throw UnauthorizedException("권한이 없습니다.")

        comment.updateComment(commentRequest)
        return CommentResponse.from(comment)
    }

    @Transactional
    fun deleteComment(
        postId: Long,
        userId: Long,
        commentId: Long
    )
    {
        postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("삭제된 게시글입니다.")
        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw ModelNotFoundException("삭제된 댓글입니다.")

        if (comment.isDeleted)
            throw ModelNotFoundException("삭제된 댓글입니다")

        if(userId != comment.user.id )
            throw UnauthorizedException("권한이 없습니다.")

        comment.softDeleted()
    }
}