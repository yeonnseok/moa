package com.moa.recommendation.domain

import com.moa.exceptions.ContentNotFoundException
import com.moa.exceptions.DescriptionNotFoundException
import org.springframework.stereotype.Component

@Component
class ContentFinder(
    private val contentRepository: ContentRepository
) {
    fun findRandomContentByScore(score: Int) =
        contentRepository.findByMinValueLessThanEqualAndMaxValueGreaterThanEqual(score, score)
            .randomOrNull() ?: throw DescriptionNotFoundException()

    fun findAll(): List<Content> {
        return contentRepository.findAllByOrderByIdAsc()
    }

    fun findById(id: Long): Content {
        return contentRepository.findById(id)
            .orElseThrow { ContentNotFoundException() }
    }
}
