package com.moa.admin.dto

import com.moa.recommendation.domain.Content
import com.moa.recommendation.domain.ContentType

data class ContentRequest(
    val title: String,
    val contents: String,
    val type: String,
    val minValue: Int,
    val maxValue: Int
) {
    fun toEntity(): Content {
        return Content(
            title = title,
            contents = contents,
            minValue = minValue,
            maxValue = maxValue,
            type = ContentType.valueOf(type)
        )
    }
}
