package com.moa.record.domain

import com.moa.exceptions.DescriptionNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DescriptionService(
    private val descriptionRepository: DescriptionRepository
) {
    fun find(score: Int): Description {
        return descriptionRepository.findByMaxValueGreaterThanEqualAndMinValueLessThanEqual(score, score)
            .randomOrNull() ?: throw DescriptionNotFoundException()
    }
}