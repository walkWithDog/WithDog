package com.teamsparta.withdog.domain.comment.model

import com.teamsparta.withdog.domain.comment.dto.CommentRequest
import com.teamsparta.withdog.domain.post.model.Post
import com.teamsparta.withdog.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(
    @Column(name="content")
    var content: String,

    @Column(name="created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name="updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name="is_deleted")
    var isDeleted: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateComment(
        commentRequest: CommentRequest
    )
    {
        content = commentRequest.content
        updatedAt = LocalDateTime.now()
    }

    fun softDeleted()
    {
        isDeleted = true
    }
}