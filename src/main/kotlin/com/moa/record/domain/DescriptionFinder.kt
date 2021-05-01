package com.moa.record.domain

import com.moa.exceptions.DescriptionNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class DescriptionFinder(
    private val descriptionRepository: DescriptionRepository
) {
    fun find(score: Int): Description {
        return descriptionRepository.findByMaxValueGreaterThanEqualAndMinValueLessThanEqual(score, score)
            .randomOrNull() ?: throw DescriptionNotFoundException()
    }
}
