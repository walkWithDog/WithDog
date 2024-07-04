package com.teamsparta.withdog.domain.post.model

import com.teamsparta.withdog.domain.post.dto.PostRequest
import com.teamsparta.withdog.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post")
class Post(
    @Column(name="title")
    var title: String,

    @Column(name="content")
    var content: String,

    @Column(name="breed_name")
    var breedName: String,

    @Column(name="created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name="updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name="is_deleted")
    var isDeleted: Boolean = false,

    @Column(name="image_url")
    var imageUrl: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updatePost(
        postRequest: PostRequest,
        image: String?) {
        title = postRequest.title
        content = postRequest.content
        imageUrl = image
        updatedAt = LocalDateTime.now()
    }

    fun softDeleted(){
        isDeleted = true
    }
}