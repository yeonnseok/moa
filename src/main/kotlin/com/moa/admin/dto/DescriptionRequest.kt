package com.moa.admin.dto

import com.moa.record.domain.Description

data class DescriptionRequest(
    val description: String,
    val minValue: Int,
    val maxValue: Int
) {
    fun toEntity(): Description {
        return Description(
            description = description,
            minValue = minValue,
            maxValue = maxValue
        )
    }
}
