package com.moa.bookmark.domain

import com.moa.recommendation.domain.Recommendation
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, Long> {

    fun findByUserId(userId: Long): List<Bookmark>

    fun findByRecommendation(exist: Recommendation): Bookmark?
}
