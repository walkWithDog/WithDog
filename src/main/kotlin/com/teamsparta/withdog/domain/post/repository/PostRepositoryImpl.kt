package com.teamsparta.withdog.domain.post.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.teamsparta.withdog.domain.post.model.Post
import com.teamsparta.withdog.domain.post.model.QPost
import com.teamsparta.withdog.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl: CustomPostRepository, QueryDslSupport() {
    private val post = QPost.post

    override fun findByIsDeletedFalseAndPageable(
        pageable: Pageable
    ): Page<Post> {
        val whereClause = BooleanBuilder()

        val totalCount = queryFactory.select(post.count())
            .from(post)
            .where(whereClause)
            .fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(post)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, post))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    private fun getOrderSpecifier(
        pageable: Pageable,
        path: EntityPathBase<*>
    ): Array<OrderSpecifier<*>> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map { order ->
            OrderSpecifier(
                if (order.isAscending) Order.ASC else Order.DESC,
                pathBuilder.get(order.property) as Expression<Comparable<*>>
            )
        }.toTypedArray()
    }

    override fun findTop10ByIsDeletedFalseOrderByViewsDesc(): List<Post> {
        return queryFactory.selectFrom(post)
            .where(post.isDeleted.isFalse)
            .orderBy(post.views.desc())
            .limit(10)
            .fetch()
    }

    override fun findByKeyword(pageable: Pageable, keyword: String): Page<Post> {
        val whereClause = BooleanBuilder()
        whereClause.and(post.title.containsIgnoreCase(keyword)
            .or(post.content.containsIgnoreCase(keyword)))
            .or(post.breedName.containsIgnoreCase(keyword))

        val totalCount = queryFactory.select(post.count())
            .from(post)
            .where(whereClause)
            .fetchCount()

        val contents = queryFactory.selectFrom(post)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, post))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}
