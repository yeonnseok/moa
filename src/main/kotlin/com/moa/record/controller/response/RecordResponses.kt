package com.moa.record.controller.response

data class RecordResponses(
    val averageScore: Int,
    val records: List<SimpleRecordResponse>
    // TODO: percentage
)
