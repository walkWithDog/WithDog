package com.teamsparta.withdog.infra.redis

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class EvictCache(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)


    fun evictCaches(breedName: String) {
        logger.info("해당 견종 캐시 삭제 : $breedName")
        val keys = redisTemplate.keys("keywordPostCache::$breedName*")
        if (!keys.isNullOrEmpty()) {
            redisTemplate.delete(keys)
            logger.info("삭제된 키들: $keys")
        } else {
            logger.info("삭제할 키가 없습니다.")
        }
    }

    @CacheEvict( value = ["keywordPostCache"], key ="#breedName")
    fun evictCaches(postId: Long, breedName: String){
        logger.info("해당 견종 캐시 삭제 : $breedName")
    }

}