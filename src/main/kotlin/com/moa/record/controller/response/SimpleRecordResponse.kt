package com.moa.record.controller.response

import com.moa.record.domain.Record
import java.time.LocalDate

data class SimpleRecordResponse(
    val recordId: Long,
    val recordDate: LocalDate,
    val score: Int
) {
    companion object {
        fun of(record: Record): SimpleRecordResponse {
            return SimpleRecordResponse(
                recordId = record.id!!,
                recordDate = record.recordDate,
                score = record.totalScore()
            )
        }
    }
}
