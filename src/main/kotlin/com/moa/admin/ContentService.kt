package com.moa.admin

import com.moa.admin.dto.ContentRequest
import com.moa.exceptions.ContentNotFoundException
import com.moa.recommendation.domain.Content
import com.moa.recommendation.domain.ContentRepository
import com.moa.recommendation.domain.ContentType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ContentService(
    private val contentRepository: ContentRepository
) {
    fun create(request: ContentRequest): Content {
        return contentRepository.save(request.toEntity())
    }

    fun update(id: Long, request: ContentRequest) {
        val content = contentRepository.findById(id)
            .orElseThrow { ContentNotFoundException() }

        content.title = request.title
        content.contents = request.contents
        content.type = ContentType.valueOf(request.type)
        content.minValue = request.minValue
        content.maxValue = request.maxValue
    }

    fun delete(id: Long) {
        contentRepository.deleteById(id)
    }
}