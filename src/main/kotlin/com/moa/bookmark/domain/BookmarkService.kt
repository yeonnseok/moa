package com.moa.bookmark.domain

import com.moa.bookmark.controller.request.BookmarkCreateRequest
import com.moa.bookmark.controller.response.BookmarkCreateResponse
import com.moa.bookmark.controller.response.BookmarkResponse
import com.moa.exceptions.BookmarkNotFoundException
import com.moa.exceptions.RecommendationNotFoundException
import com.moa.exceptions.RecordNotFoundException
import com.moa.recommendation.domain.RecommendationRepository
import com.moa.record.domain.RecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BookmarkService(
    private val recommendationRepository: RecommendationRepository,
    private val recordRepository: RecordRepository,
    private val bookmarkRepository: BookmarkRepository
) {
    @Transactional
    fun create(userId: Long, request: BookmarkCreateRequest): BookmarkCreateResponse {
        val recommendation = findRecommendation(request.recommendationId)
        val bookmark = bookmarkRepository.save(
            Bookmark(
                userId = userId,
                recommendation = recommendation
            )
        )
        return BookmarkCreateResponse(bookmark.id!!)
    }

    private fun findRecommendation(id: Long) =
        recommendationRepository.findById(id)
            .orElseThrow { RecommendationNotFoundException() }

    fun findList(userId: Long): List<BookmarkResponse> {
        return bookmarkRepository.findByUserId(userId)
            .map {
                val record = findRecord(it.recommendation.recordId)
                BookmarkResponse.of(record, it.recommendation)
            }
    }

    fun findDetail(id: Long): BookmarkResponse {
        val bookmark = bookmarkRepository.findById(id)
            .orElseThrow { BookmarkNotFoundException() }
        val record = findRecord(bookmark.recommendation.recordId)
        return BookmarkResponse.of(record, bookmark.recommendation)
    }

    private fun findRecord(id: Long) =
        recordRepository.findById(id)
            .orElseThrow { RecordNotFoundException() }

    @Transactional
    fun delete(id: Long) {
        bookmarkRepository.deleteById(id)
    }
}
