package com.teamsparta.withdog.domain.post.dto

import java.io.Serializable

data class PageResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int
) : Serializable
