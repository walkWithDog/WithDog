package com.teamsparta.withdog.domain.comment.controller

import com.teamsparta.withdog.domain.comment.dto.CommentRequest
import com.teamsparta.withdog.domain.comment.dto.CommentResponse
import com.teamsparta.withdog.domain.comment.service.CommentService
import com.teamsparta.withdog.infra.security.jwt.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
class CommentController(
    private val commentService: CommentService
)
{
    @PostMapping
    fun createComment(
        @PathVariable("postId") postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody commentRequest: CommentRequest
    ): ResponseEntity<CommentResponse>
    {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.createComment(postId, principal.id, commentRequest))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable commentId: Long,
        @RequestBody @Valid commentRequest: CommentRequest
    ): ResponseEntity<CommentResponse>
    {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(postId, principal.id, commentId, commentRequest))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable commentId: Long
    ): ResponseEntity<Unit>
    {
        commentService.deleteComment(postId, principal.id, commentId)

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}