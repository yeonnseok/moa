package com.moa.bookmark.controller.response

import com.moa.recommendation.domain.ContentType
import com.moa.recommendation.domain.Recommendation
import com.moa.record.domain.Record
import java.time.LocalDate

data class BookmarkResponse(
    val recordId: Long,
    val recordDate: LocalDate,
    val score: Int,
    val recommendationId: Long,
    val title: String,
    val contents: String,
    val type: ContentType,
    val contentId: Long
) {
    companion object {
        fun of(record: Record, recommendation: Recommendation): BookmarkResponse {
            return BookmarkResponse(
                recordId = record.id!!,
                recordDate = record.recordDate,
                score = record.totalScore(),
                recommendationId = recommendation.id!!,
                title = recommendation.content.title,
                contents = recommendation.content.contents,
                type = recommendation.content.type,
                contentId = recommendation.content.id!!
            )
        }
    }
}
