package com.moa.record.controller.response

data class RecordResponses(
    val averageScore: Int,
    val empathyPercentage: Int,
    val records: List<SimpleRecordResponse>
)
