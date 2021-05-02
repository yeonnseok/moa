package com.moa.bookmark.domain

import com.moa.bookmark.controller.request.BookmarkCreateRequest
import com.moa.bookmark.controller.response.BookmarkCreateResponse
import com.moa.exceptions.RecommendationNotFoundException
import com.moa.recommendation.domain.RecommendationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BookmarkService(
    private val recommendationRepository: RecommendationRepository,
    private val bookmarkRepository: BookmarkRepository
) {
    fun create(userId: Long, request: BookmarkCreateRequest): BookmarkCreateResponse {
        val recommendation = recommendationRepository.findById(request.recommendationId)
            .orElseThrow { RecommendationNotFoundException() }

        val bookmark = bookmarkRepository.save(
            Bookmark(
                userId = userId,
                recommendation = recommendation
            )
        )
        return BookmarkCreateResponse(bookmark.id!!)
    }
}
