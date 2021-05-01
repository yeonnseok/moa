package com.moa.acceptance

import com.moa.common.ResultType
import com.moa.record.controller.request.RecordCreateRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.stream.Stream

class RecordAcceptanceTest : AcceptanceTest() {

    @DisplayName("감정 기록 인수테스트")
    @TestFactory
    fun manageRecord(): Stream<DynamicTest> {
        return Stream.of(
            DynamicTest.dynamicTest("데일리 감정 기록하기", {
                // given
                val request = RecordCreateRequest(
                    recordDate = LocalDate.of(2021, 5, 5),
                    emotions = mapOf("happy" to 10),
                    keywords = setOf("study", "money"),
                    memo = "first record"
                )

                // when
                val response = post("/api/v1/records", request)

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.CREATED.value()
            }),

            DynamicTest.dynamicTest("일일 감정 기록 조회하기", {
                // when
                val response = get("/api/v1/records?recordDate=2021-05-05")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()
            }),

            DynamicTest.dynamicTest("주간 감정 기록 조회하기", {
                // when
                val response = get("/api/v1/records?fromDate=2021-05-01&toDate=2021-05-08")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()
            })
        )
    }
}