package com.teamsparta.withdog.domain.api


import com.teamsparta.withdog.domain.post.dto.PostRequest
import com.teamsparta.withdog.domain.post.dto.PostResponse
import com.teamsparta.withdog.domain.post.model.Post
import com.teamsparta.withdog.domain.post.repository.PostRepository
import com.teamsparta.withdog.domain.post.service.PostService
import com.teamsparta.withdog.domain.user.dto.UserSignUpRequest
import com.teamsparta.withdog.domain.user.model.User
import com.teamsparta.withdog.domain.user.model.UserProfile
import com.teamsparta.withdog.domain.user.repository.UserRepository
import com.teamsparta.withdog.domain.user.service.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
class PostServiceTest{

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var postService: PostService

    @Autowired
    private lateinit var cacheManager: CacheManager



    @Transactional
    @Test
    fun ` post를 생성하면 PopularKeywords 리스트에 해당 견종이름이 들어가는지 확인하는 테스트 함수   `(){

        val request = postService.createPost(
            image = null,
            userId = 1,
            postRequest = PostRequest(
                title = "테스트코드용 제목",
                content = "테스트 코드용 내용",
                breedName = "푸들"
            )
        )

        postRepository.findPopularKeywords()
            .let{
                it.contains(request.breedName) shouldNotBe null
            }

    }

    @Transactional
    @Test
    fun `캐시를 적용한 검색 api가 처음 조회했을때 캐시에 등록이 되는지 테스트 하는 함수`(){


        val postResponse =postService.createPost(
            image = null,
            userId = 1,
            postRequest = PostRequest(
                title = "캐시 테스트코드용 제목",
                content = "캐시 테스트 코드용 내용",
                breedName = "해리포터"
            )
        )
        val post = postRepository.findByIdOrNull(postResponse.id)!!
        val postId = post.id!!


        cacheManager.getCache("postCache")!!.get(postId) shouldBe null

        postService.getPostById(postId)

        cacheManager.getCache("postCache")!!.get(postId) shouldNotBe null

        val cachedPost =cacheManager.getCache("postCache")!!.get(postId)!!.get() as PostResponse
        cachedPost.id shouldBe postId
        cachedPost.title shouldBe "캐시 테스트코드용 제목"

        postService.deletePost(userId = 1, postId = postId)

        cacheManager.getCache("postCache")!!.get(postId) shouldBe null

    }

    @Transactional
    @Test
    fun `새로운 포스트를 만들면 해당 포스트의 견종에 대한 검색 리스트의 캐시가 삭제되는지 테스트하는 함수`(){

        postService.getPostByKeyword(
            page= 0,
            size= 10,
            sortBy= "createdAt",
            direction= "desc",
            keyword= "해리포터"
        )
        cacheManager.getCache("keywordPostCache")!!.get("해리포터-10-0") shouldNotBe null

        //조회 했으니 캐시가 있어야 하며 create를 하면 캐시가 삭제되어야함

        postService.createPost(
            image = null,
            userId = 1,
            postRequest = PostRequest(
                title = "캐시 테스트코드용 제목",
                content = "캐시 테스트 코드용 내용",
                breedName = "해리포터"
            )
        )

        cacheManager.getCache("keywordPostCache")!!.get("해리포터-10-0") shouldBe null

    }

    @Transactional
    @Test
    fun `캐시를 적용하지 않은 키워드검색api를 이용하면 실제로 캐시가 생성되지 않는지 테스트하는 함수`(){

        postService.createPost(
            image = null,
            userId = 1,
            postRequest = PostRequest(
                title = "캐시 테스트코드용 제목",
                content = "캐시 테스트 코드용 내용",
                breedName = "스타워즈"
            )
        )
        cacheManager.getCache("keywordPostCache")!!.get("스타워즈") shouldBe null

        postService.getPostByKeywordNoCache(
            page= 1,
            size= 7,
            sortBy= "createdAt",
            direction= "desc",
            keyword= "스타워즈"
        )

        cacheManager.getCache("keywordPostCache")!!.get("스타워즈-7-1") shouldBe null

    }

    @Transactional
    @Test
    fun `페이지네이션이 적용된 키워드 검색을 했을때  페이지네이션 캐시가 잘 저장되는지 테스트하는 함수`(){

        postService.createPost(
            image = null,
            userId = 1,
            postRequest = PostRequest(
                title = "캐시 테스트코드용 제목",
                content = "캐시 테스트 코드용 내용",
                breedName = "티라노사우로스"
            )
        )

        postService.getPostByKeyword(
            page= 2,
            size= 3,
            sortBy= "createdAt",
            direction= "desc",
            keyword= "티라노사우로스"
        )

        cacheManager.getCache("keywordPostCache")!!.get("티라노사우로스-3-2") shouldNotBe null

    }

    @Transactional
    @Test
    fun `게시글을 삭제했을때 해당 게시글의 견종 관련 페이지네이션 캐시가 다 삭제가 되는지 테스트하는 함수`(){

        val postResponse = postService.createPost(
            image = null,
            userId = 1,
            postRequest = PostRequest(
                title = "치와와 좋아요",
                content = "안녕하세요 오늘은...",
                breedName = "치와와"
            )
        )
        val post = postRepository.findByIdOrNull(postResponse.id)!!
        val postId = post.id!!

        postService.getPostByKeyword(
            page= 0,
            size= 3,
            sortBy= "createdAt",
            direction= "desc",
            keyword= "치와와"
        )

        postService.getPostByKeyword(
            page= 0,
            size= 2,
            sortBy= "createdAt",
            direction= "desc",
            keyword= "치와와"
        )

        postService.getPostByKeyword(
            page= 1,
            size= 2,
            sortBy= "createdAt",
            direction= "desc",
            keyword= "치와와"
        )

        cacheManager.getCache("keywordPostCache")!!.get("치와와-3-0") shouldNotBe null
        cacheManager.getCache("keywordPostCache")!!.get("치와와-2-0") shouldNotBe null
        cacheManager.getCache("keywordPostCache")!!.get("치와와-2-1") shouldNotBe null

        postService.deletePost(postId = postId, userId = 1)

        cacheManager.getCache("keywordPostCache")!!.get("치와와-3-0") shouldBe null
        cacheManager.getCache("keywordPostCache")!!.get("치와와-2-0") shouldBe null
        cacheManager.getCache("keywordPostCache")!!.get("치와와-2-1") shouldBe null
        cacheManager.getCache("postCache")!!.get(postId) shouldBe null


    }




}