package com.moa.record.domain

import com.moa.exceptions.RecordExistedSameDayException
import com.moa.exceptions.RecordNotFoundException
import com.moa.exceptions.UnAuthorizedException
import com.moa.record.controller.request.RecordCreateRequest
import com.moa.record.controller.request.RecordUpdateRequest
import com.moa.record.controller.response.RecordResponse
import com.moa.record.controller.response.WeeklyRecordResponse
import com.moa.record.controller.response.SimpleRecordResponse
import com.moa.record.controller.response.WeeklyEmotionStatic
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(javaClass)

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

    fun findWeekly(userId: Long, fromDate: LocalDate, toDate: LocalDate): WeeklyRecordResponse {
        val records = recordRepository.findByUserIdAndRecordDateGreaterThanEqualAndRecordDateLessThanEqual(
            userId = userId,
            fromDate = fromDate,
            toDate = toDate
        )

        val averageScore = calcAverageScore(records)
        val empathyPercentage = getEmpathyPercentage(averageScore, fromDate, toDate)
        val weeklyEmotionStatic = getEmotionStatic(records)

        return WeeklyRecordResponse(
            averageScore = averageScore,
            empathyPercentage = empathyPercentage,
            weeklyEmotionStatic = weeklyEmotionStatic
        )
    }

    private fun getEmotionStatic(records: List<Record>): WeeklyEmotionStatic {
        return WeeklyEmotionStatic(
            happy = records.map { it.emotions.filter{ it.emotionType == EmotionType.HAPPY }.firstOrNull()?.count ?: 0 * EmotionType.HAPPY.score }.sum(),
            flutter = records.map { it.emotions.filter{ it.emotionType == EmotionType.FLUTTER }.firstOrNull()?.count ?: 0 * EmotionType.FLUTTER.score }.sum(),
            proud = records.map { it.emotions.filter{ it.emotionType == EmotionType.PROUD }.firstOrNull()?.count ?: 0 * EmotionType.PROUD.score }.sum(),
            nervous = records.map { it.emotions.filter{ it.emotionType == EmotionType.NERVOUS }.firstOrNull()?.count ?: 0 * EmotionType.NERVOUS.score }.sum(),
            sad = records.map { it.emotions.filter{ it.emotionType == EmotionType.SAD }.firstOrNull()?.count ?: 0 * EmotionType.SAD.score }.sum(),
            annoy = records.map { it.emotions.filter{ it.emotionType == EmotionType.ANNOY }.firstOrNull()?.count ?: 0 * EmotionType.ANNOY.score }.sum(),
            angry = records.map { it.emotions.filter{ it.emotionType == EmotionType.ANGRY }.firstOrNull()?.count ?: 0 * EmotionType.ANGRY.score }.sum()
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

    @Transactional
    fun update(userId: Long, recordId: Long, request: RecordUpdateRequest): RecordResponse {
        validateAuthor(userId, recordId)
        val record = recordRepository.findById(recordId)
                .orElseThrow { RecordNotFoundException() }

        record.memo = request.memo
        val description = descriptionFinder.find(record.totalScore())
        return RecordResponse.of(record, description.description)
    }

    @Transactional
    fun delete(userId: Long, recordId: Long) {
        validateAuthor(userId, recordId)
        recordRepository.deleteById(recordId)
    }

    private fun validateAuthor(userId: Long, recordId: Long) {
        val record = recordRepository.findById(recordId)
                .orElseThrow { RecordNotFoundException() }

        if (record.userId != userId) {
            throw UnAuthorizedException()
        }
    }
}
