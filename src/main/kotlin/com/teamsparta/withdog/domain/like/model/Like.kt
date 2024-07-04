package com.teamsparta.withdog.domain.like.model

import com.teamsparta.withdog.domain.post.model.Post
import com.teamsparta.withdog.domain.user.model.User
import jakarta.persistence.*

@Entity
@Table(name = "likes")
class Like(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}