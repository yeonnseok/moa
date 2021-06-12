package com.moa.recommendation.domain

import com.moa.recommendation.domain.dto.RecommendationResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RecommendationService(
    private val contentFinder: ContentFinder,
    private val recommendationRepository: RecommendationRepository
) {
    @Transactional
    fun recommend(userId: Long, recordId: Long, score: Int): RecommendationResponse {
        val exist = recommendationRepository.findByRecordId(recordId)

        if (exist != null) {
            return RecommendationResponse.of(exist)
        }

        val content = contentFinder.findRandomContentByScore(score)
        val recommendation = recommendationRepository.save(
            Recommendation(
                userId = userId,
                recordId = recordId,
                content = content
            )
        )
        return RecommendationResponse.of(recommendation)
    }
}
