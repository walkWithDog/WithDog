package com.teamsparta.withdog.domain.post.service

import com.teamsparta.withdog.domain.comment.service.CommentService
import com.teamsparta.withdog.domain.like.service.LikeService
import com.teamsparta.withdog.domain.post.dto.*

import com.teamsparta.withdog.domain.post.repository.PostRepository
import com.teamsparta.withdog.domain.user.repository.UserRepository
import com.teamsparta.withdog.global.exception.ModelNotFoundException
import com.teamsparta.withdog.global.exception.UnauthorizedException
import com.teamsparta.withdog.infra.redis.EvictCache
import com.teamsparta.withdog.infra.redis.ViewCount
import com.teamsparta.withdog.infra.s3.S3Service
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val s3Service: S3Service,
    private val likeService: LikeService,
    private val commentService: CommentService,
    private val viewCount: ViewCount,
    private val evictCache: EvictCache,

) {
    private val logger = LoggerFactory.getLogger(this::class.java)


    @Cacheable(value = ["popularPostCache"], key = "'getPopularPostList'") // 인기검색어 목록은 1시간마다 갱신됨
    fun getPopularPostList()
            : List<PopularPostResponse> {
        val popularPosts = postRepository.findTop10ByIsDeletedFalseOrderByViewsDesc()
        return popularPosts.map { PopularPostResponse.from(it) }
    }


    fun getPostList(
        page: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): PageResponse<PostResponse> {
        val direction = getDirection(direction)
        val pageable: Pageable = PageRequest.of(page, size, direction, sortBy)

        val pageContent = postRepository.findByIsDeletedFalseAndPageable(pageable)

        return PageResponse(
            pageContent.content.map { PostResponse.from(it, commentService.getCommentList(it.id!!)) },
            page,
            size
        )
    }

    @Cacheable(value = ["postCache"], key = "#postId")
    fun getPostById(postId: Long): PostResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("삭제된 게시글 입니다.")
        if (post.isDeleted) throw ModelNotFoundException("삭제된 게시글 입니다.")


        viewCount.incrementViewCount(postId) // 조회수 증가
        return PostResponse.from(post, commentService.getCommentList(postId))
    }

    @Cacheable(value = ["keywordPostCache"], key = "#keyword")
    fun getPostByKeyword(
        page: Int,
        size: Int,
        sortBy: String,
        direction: String,
        keyword: String
    ): PageResponse<PostResponse> {
        val direction = getDirection(direction)
        val pageable: Pageable = PageRequest.of(page, size, direction, sortBy)
        val pageContent = postRepository.findByKeyword(pageable, keyword)

        return PageResponse(
            pageContent.content.map { PostResponse.from(it, commentService.getCommentList(it.id!!)) },
            page,
            size
        )

    }

    fun getPostByKeywordNoCache(
        page: Int,
        size: Int,
        sortBy: String,
        direction: String,
        keyword: String
    ): PageResponse<PostResponse> {
        val direction = getDirection(direction)
        val pageable: Pageable = PageRequest.of(page, size, direction, sortBy)
        val pageContent = postRepository.findByKeyword(pageable, keyword)

        return PageResponse(
            pageContent.content.map { PostResponse.from(it, commentService.getCommentList(it.id!!)) },
            page,
            size
        )
    }





    @CacheEvict(value = ["keywordPostCache"], key = "#postRequest.breedName")
    @Transactional
    fun createPost(
        userId: Long,
        postRequest: PostRequest,
        image: MultipartFile?
    ): PostResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ModelNotFoundException("없는 사용자 입니다.")
        val fileUrl = image?.let { s3Service.upload(it) }

        return PostResponse.from(postRepository.save(postRequest.toEntity(user, fileUrl)), null)
    }



    @CachePut(value = ["postCache"], key = "#postId")
    @Transactional
    fun updatePost(
        postId: Long,
        userId: Long,
        postRequest: PostRequest,
        image: MultipartFile?
    ): PostResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("삭제된 게시글 입니다.")

        if (post.isDeleted)
            throw ModelNotFoundException("삭제된 게시글 입니다.")

        if (userId != post.user.id)
            throw UnauthorizedException("권한이 없습니다.")

        post.imageUrl?.let { s3Service.delete(it.split("m/")[1]) }
        val imageUrl = image?.let { s3Service.upload(it) }

        post.updatePost(postRequest, imageUrl)
        return PostResponse.from(post, commentService.getCommentList(postId))
    }


    @Caching(
        evict = [
            CacheEvict(value = ["postCache"], key = "#postId"),
        ]
    )
    @Transactional
    fun deletePost(
        postId: Long,
        userId: Long
    ) {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("삭제된 게시글 입니다.")

        if (post.isDeleted)
            throw ModelNotFoundException("삭제된 게시글 입니다.")

        if (userId != post.user.id)
            throw UnauthorizedException("권한이 없습니다.")

        evictCache.evictCaches(breedName = post.breedName, postId = postId)
        post.imageUrl?.let { s3Service.delete(it.split("m/")[1]) }
        post.softDeleted()
        likeService.deleteLike(post)


    }



    fun postLike(
        postId: Long,
        userId: Long
    ) {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("삭제된 게시글입니다.")

        if (post.isDeleted)
            throw ModelNotFoundException("삭제된 게시글 입니다.")

        likeService.updateLike(userId, post)
    }

    private fun getDirection(sort: String) = when (sort) {
        "asc" -> Sort.Direction.ASC
        else -> Sort.Direction.DESC
    }

    fun getPopularKeywordList() {

    }


}