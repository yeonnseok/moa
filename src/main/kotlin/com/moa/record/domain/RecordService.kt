package com.moa.record.domain

import com.moa.exceptions.RecordExistedSameDayException
import com.moa.exceptions.RecordNotFoundException
import com.moa.record.controller.request.RecordCreateRequest
import com.moa.record.controller.response.RecordResponse
import com.moa.record.controller.response.RecordResponses
import com.moa.record.controller.response.SimpleRecordResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.roundToInt

@Service
@Transactional(readOnly = true)
class RecordService(
    private val descriptionFinder: DescriptionFinder,
    private val recordRepository: RecordRepository
) {
    private val numberOfWeekDay = 7

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

    fun findDaily(userId: Long, recordDate: LocalDate): RecordResponse {
        val record = recordRepository.findByUserIdAndRecordDate(userId, recordDate)
            ?: throw RecordNotFoundException()

        val description = descriptionFinder.find(record.totalScore())
        return RecordResponse.of(record, description.description)
    }

    fun findWeekly(userId: Long, fromDate: LocalDate, toDate: LocalDate): RecordResponses {
        val records = recordRepository.findByUserIdAndRecordDateGreaterThanEqualAndRecordDateLessThanEqual(
            userId = userId,
            fromDate = fromDate,
            toDate = toDate
        )

        val averageScore = calcAverageScore(records)
        val empathyPercentage = getEmpathyPercentage(averageScore, fromDate, toDate)

        return RecordResponses(
            averageScore = averageScore,
            records = records.map { SimpleRecordResponse.of(it) },
            empathyPercentage = empathyPercentage
        )
    }

    private fun getEmpathyPercentage(averageScore: Int, fromDate: LocalDate, toDate: LocalDate): Int {
        val weeklyRecords = recordRepository.findByRecordDateGreaterThanEqualAndRecordDateLessThanEqual(fromDate, toDate)
        val recordedUserIds = weeklyRecords.map { it.userId }.distinct()

        val averageScores = recordedUserIds.map { userId ->
            val filteredRecords = weeklyRecords.filter { it.userId == userId}
            calcAverageScore(filteredRecords)
        }

        val empathyCount = averageScores.filter { it == averageScore }.count()
        val totalRecordedCount = recordedUserIds.count()

        return BigDecimal(100)
            .multiply(BigDecimal(empathyCount))
            .div(BigDecimal(totalRecordedCount))
            .toInt()
    }

    private fun calcAverageScore(records: List<Record>) =
        records.map { it.totalScore() }
            .sum()
            .toDouble()
            .div(numberOfWeekDay)
            .roundToInt()

}
