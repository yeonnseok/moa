package com.moa.record.domain

import com.moa.exceptions.RecordExistedSameDayException
import com.moa.exceptions.RecordNotFoundException
import com.moa.record.controller.request.RecordCreateRequest
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDate

@SpringBootTest
@Sql("/truncate.sql")
internal class RecordServiceTest {

    @Autowired
    private lateinit var sut: RecordService

    @Autowired
    private lateinit var recordRepository: RecordRepository

    @Test
    fun `감정 기록 실패 - 당일 기록 존재`() {
        // given
        recordRepository.save(
            Record(
                userId = 1,
                recordDate = LocalDate.of(2021,5,5),
                keywords = setOf(Keyword.STUDY, Keyword.MONEY),
                memo = "first memo"
            )
        )

        val request = RecordCreateRequest(
            recordDate = LocalDate.of(2021,5,5),
            emotions = mapOf("happy" to 10),
            keywords = setOf("study", "money"),
            memo = "second memo"
        )

        // when
        val response = shouldThrow<RecordExistedSameDayException> { sut.create(1, request) }

        // then
        response.message shouldBe "해당 일에 기록이 이미 존재합니다."
    }

    @Test
    fun `감정 기록 성공`() {
        // given
        val request = RecordCreateRequest(
            recordDate = LocalDate.of(2021,5,5),
            emotions = mapOf("happy" to 10),
            keywords = setOf("study", "money"),
            memo = "first memo"
        )

        // when
        val recordId = sut.create(1, request)

        // then
        recordId shouldBe 1
    }

    @Test
    fun `감정 기록 조회 실패 - 해당 날에 기록 없음`() {
        // when
        val response = shouldThrow<RecordNotFoundException> { sut.find(1, LocalDate.of(2021,5,5)) }

        // then
        response.message shouldBe "감정 기록이 존재하지 않습니다."
    }

    @Test
    fun `감정 기록 조회`() {
        // given
        recordRepository.save(
            Record(
                userId = 1,
                recordDate = LocalDate.of(2021,5,5),
                keywords = setOf(Keyword.STUDY, Keyword.MONEY),
                memo = "first memo"
            )
        )

        // when
        val record = sut.find(1, LocalDate.of(2021,5,5))

        // then
        record.userId shouldBe 1
        record.recordDate shouldBe LocalDate.of(2021,5,5)
        record.keywords shouldBe setOf(Keyword.STUDY, Keyword.MONEY)
        record.memo shouldBe "first memo"
    }
}
