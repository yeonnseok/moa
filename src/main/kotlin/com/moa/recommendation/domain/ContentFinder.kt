package com.moa.recommendation.domain

import com.moa.exceptions.DescriptionNotFoundException
import org.springframework.stereotype.Component

@Component
class ContentFinder(
    private val contentRepository: ContentRepository
) {
    fun findRandomContentByScore(score: Int) =
        contentRepository.findByMinValueLessThanEqualAndMaxValueGreaterThanEqual(score, score)
            .randomOrNull() ?: throw DescriptionNotFoundException()
}
