package com.teamsparta.withdog.infra.redis

import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class ViewCount {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val viewCounts = ConcurrentHashMap<Long, Long>()

    fun incrementViewCount(postId: Long) {
        viewCounts.merge(postId, 1) { oldValue, _ ->
            val newValue = oldValue + 1
            logger.info("해당 postId: $postId. 조회수 카운트: $newValue")
            newValue
        }
    }

    fun getViewCountsAndClear(): Map<Long, Long> {
        val copy = HashMap(viewCounts)
        viewCounts.clear()
        logger.info("조회수 초기화. 현재 카운트: $copy")
        return copy
    }
}