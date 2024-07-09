package com.teamsparta.withdog.infra.redis

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Component


@Component
class EvictCache {
    private val logger = LoggerFactory.getLogger(this.javaClass)


    @CacheEvict( value = ["keywordPostCache"], key ="#breedName + '*' ")
    fun evictCaches(postId: Long, breedName: String){
        logger.info("해당 견종 캐시 삭제 : $breedName")
    }
}