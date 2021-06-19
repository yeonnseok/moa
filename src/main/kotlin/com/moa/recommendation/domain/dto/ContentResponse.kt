package com.moa.recommendation.domain.dto

import com.moa.recommendation.domain.Content

data class ContentResponse(
    val id: Long,
    val title: String,
    val contents: String,
    val type: String,
    val description: String
) {
    companion object {
        fun of(content: Content, description: String): ContentResponse {
            return ContentResponse(
                id = content.id!!,
                title = content.title,
                contents = content.contents,
                type = content.type.name,
                description = description
            )
        }
    }
}
