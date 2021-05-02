package com.moa.bookmark.controller.response

import com.moa.recommendation.domain.dto.RecommendationResponse
import com.moa.record.controller.response.SimpleRecordResponse

data class BookmarkResponse(
    val record: SimpleRecordResponse,
    val recommendation: RecommendationResponse
)
