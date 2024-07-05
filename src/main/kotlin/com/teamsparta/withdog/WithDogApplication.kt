package com.teamsparta.withdog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@EnableCaching // 요거 없어도 동작은 하긴 함
@EnableScheduling
@SpringBootApplication
class WithDogApplication

fun main(args: Array<String>) {
    runApplication<WithDogApplication>(*args)
}
