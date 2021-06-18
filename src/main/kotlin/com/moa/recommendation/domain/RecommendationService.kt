package com.moa.recommendation.domain

import com.moa.bookmark.domain.BookmarkRepository
import com.moa.exceptions.RecordNotFoundException
import com.moa.recommendation.domain.dto.RecommendationResponse
import com.moa.record.domain.DescriptionFinder
import com.moa.record.domain.RecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RecommendationService(
    private val contentFinder: ContentFinder,
    private val descriptionFinder: DescriptionFinder,
    private val recordRepository: RecordRepository,
    private val recommendationRepository: RecommendationRepository,
    private val bookmarkRepository: BookmarkRepository
) {
    @Transactional
    fun recommend(userId: Long, recordId: Long): RecommendationResponse {
        val exist = recommendationRepository.findByRecordId(recordId)

        if (exist != null) {
            val bookmark = bookmarkRepository.findByRecommendation(exist)
            return RecommendationResponse.of(exist, bookmark?.id)
        }

        val record = recordRepository.findById(recordId)
            .orElseThrow { RecordNotFoundException() }
        val content = contentFinder.findRandomContentByScore(record.totalScore())
        val description = descriptionFinder.find(record.totalScore())

        val recommendation = recommendationRepository.save(
            Recommendation(
                userId = userId,
                recordId = recordId,
                content = content,
                description = description.description
            )
        )

        return RecommendationResponse.of(recommendation, null)
    }
}
