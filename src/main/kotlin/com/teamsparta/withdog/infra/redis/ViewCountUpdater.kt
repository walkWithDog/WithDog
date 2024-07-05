package com.teamsparta.withdog.infra.redis

import com.teamsparta.withdog.domain.post.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ViewCountUpdater(
    private val postRepository: PostRepository,
    private val viewCount: ViewCount
) {

    @Scheduled(fixedRate = 60000) // 1분 간격
    @Transactional
    fun updateViewCounts() {
        val viewCounts = viewCount.getViewCountsAndClear()
        for ((postId, count) in viewCounts) {
            val post = postRepository.findByIdOrNull(postId)
            if (post != null &&!post.isDeleted) {
                post.views += count
                postRepository.save(post)
            }
        }
    }
}