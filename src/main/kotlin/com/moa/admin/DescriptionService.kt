package com.moa.admin

import com.moa.admin.dto.DescriptionRequest
import com.moa.exceptions.DescriptionNotFoundException
import com.moa.record.domain.Description
import com.moa.record.domain.DescriptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DescriptionService(
    private val descriptionRepository: DescriptionRepository
) {
    fun create(request: DescriptionRequest): Description {
        return descriptionRepository.save(request.toEntity())
    }

    fun update(id: Long, request: DescriptionRequest) {
        val description = descriptionRepository.findById(id)
            .orElseThrow { DescriptionNotFoundException() }

        description.description = request.description
        description.minValue = request.minValue
        description.maxValue = request.maxValue
    }

    fun delete(id: Long) {
        descriptionRepository.deleteById(id)
    }
}