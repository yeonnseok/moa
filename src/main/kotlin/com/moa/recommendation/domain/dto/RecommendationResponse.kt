package com.moa.recommendation.domain.dto

import com.moa.recommendation.domain.ContentType
import com.moa.recommendation.domain.Recommendation

data class RecommendationResponse(
    val contentId: Long,
    val description: String,
    val recommendationId: Long,
    val title: String,
    val contents: String,
    val type: ContentType,
    val bookmarkId: Long?
) {
    companion object {
        fun of(recommendation: Recommendation, bookmarkId: Long?): RecommendationResponse {
            return RecommendationResponse(
                contentId = recommendation.content.id!!,
                description = recommendation.description,
                recommendationId = recommendation.id!!,
                title = recommendation.content.title,
                contents = recommendation.content.contents,
                type = recommendation.content.type,
                bookmarkId = bookmarkId
            )
        }
    }
}
