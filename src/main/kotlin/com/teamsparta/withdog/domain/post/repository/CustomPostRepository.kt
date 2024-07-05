package com.teamsparta.withdog.domain.post.repository

import com.teamsparta.withdog.domain.post.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPostRepository
{
    fun findByIsDeletedFalseAndPageable(pageable: Pageable): Page<Post>

    fun findTop10ByIsDeletedFalseOrderByViewsDesc(): List<Post>

    fun findByKeyword(pageable: Pageable,keyword: String): Page<Post>


}