package com.moa.record.controller.response

data class WeeklyRecordResponse(
    val averageScore: Int,
    val empathyPercentage: Int,
    val weeklyEmotionStatic: WeeklyEmotionStatic
)
