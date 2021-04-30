package com.moa.record.domain

import com.moa.exceptions.RecordExistedSameDayException
import com.moa.exceptions.RecordNotFoundException
import com.moa.record.controller.request.RecordCreateRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class RecordService(
    private val recordRepository: RecordRepository
) {
    @Transactional
    fun create(userId: Long, request: RecordCreateRequest): Long {
        validateExistRecord(userId, request.recordDate)

        val record = Record(
            userId = userId,
            recordDate = request.recordDate,
            keywords = collectToKeywordSet(request.keywords),
            memo = request.memo
        )
        record.addEmotions(request.emotions)
        return recordRepository.save(record).id!!
    }

    private fun validateExistRecord(userId: Long, recordDate: LocalDate) {
        if (recordRepository.existsByUserIdAndRecordDate(userId, recordDate)) {
            throw RecordExistedSameDayException()
        }
    }

    private fun collectToKeywordSet(keywords: Set<String>?): Set<Keyword>? {
        return keywords?.map { Keyword.of(it) }?.toSet()
    }

    fun find(userId: Long, recordDate: LocalDate): Record {
        return recordRepository.findByUserIdAndRecordDate(userId, recordDate)
            ?: throw RecordNotFoundException()
    }
}
