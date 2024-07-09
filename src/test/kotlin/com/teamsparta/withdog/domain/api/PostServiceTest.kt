package com.teamsparta.withdog.domain.api


import com.teamsparta.withdog.domain.post.dto.PostRequest
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
    }

}