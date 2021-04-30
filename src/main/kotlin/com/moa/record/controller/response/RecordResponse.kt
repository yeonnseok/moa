package com.moa.record.controller.response

import com.moa.record.domain.Keyword
import com.moa.record.domain.Record
import java.time.LocalDate

data class RecordResponse(
    val recordId: Long,
    val userId: Long,
    val recordDate: LocalDate,
    val emotions: List<EmotionResponse>,
    val keywords: Set<Keyword>?,
    val memo: String?,
    val score: Int = 0
) {
    companion object {
        fun of(record: Record): RecordResponse {
            return RecordResponse(
                recordId = record.id!!,
                userId = record.userId,
                recordDate = record.recordDate,
                emotions = record.emotions.map { EmotionResponse.of(it) },
                keywords = record.keywords,
                memo = record.memo,
                score = record.totalScore()
            )
        }
    }
}
