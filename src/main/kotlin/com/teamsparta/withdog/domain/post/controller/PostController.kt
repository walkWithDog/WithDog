package com.teamsparta.withdog.domain.post.controller

import com.teamsparta.withdog.domain.post.dto.PageResponse
import com.teamsparta.withdog.domain.post.dto.PopularPostResponse
import com.teamsparta.withdog.domain.post.dto.PostRequest
import com.teamsparta.withdog.domain.post.dto.PostResponse
import com.teamsparta.withdog.domain.post.service.PostService
import com.teamsparta.withdog.infra.redis.ViewCount
import com.teamsparta.withdog.infra.security.jwt.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService,
    private val viewCount: ViewCount,
) {


    @GetMapping("/keyword")
    fun getPostListByKeyword(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "createdAt") sortBy: String,
        @RequestParam("sort_direction", defaultValue = "desc") direction: String,
        @RequestParam("keyword", defaultValue = "") keyword: String,
    ): ResponseEntity<PageResponse<PostResponse>>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostByKeyword(page, size, sortBy, direction,keyword))
    }


    @GetMapping("/popular")
    fun getPopularList()
            : ResponseEntity<List<PopularPostResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPopularPostList())
    }


    @GetMapping
    fun getPostList(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "createdAt") sortBy: String,
        @RequestParam("sort_direction", defaultValue = "desc") direction: String,
    ): ResponseEntity<PageResponse<PostResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostList(page, size, sortBy, direction))
    }

    @GetMapping("/{postId}")
    fun getPostById(
        @PathVariable postId: Long
    ): ResponseEntity<PostResponse> {
        val getPost = postService.getPostById(postId)
        viewCount.incrementViewCount(postId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(getPost)
    }

    @PostMapping
    fun createPost(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestPart("request") postRequest: PostRequest,
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.createPost(principal.id, postRequest, image))
    }

    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestPart("request") postRequest: PostRequest,
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.updatePost(postId, principal.id, postRequest, image))
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(postService.deletePost(postId, principal.id))
    }

    @PostMapping("/{postId}/like")
    fun postLike(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        postService.postLike(postId, principal.id)

        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}
