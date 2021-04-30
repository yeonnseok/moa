package com.moa.record.controller

import com.moa.common.ResultType
import com.moa.record.domain.*
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

class RecordControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var recordRepository: RecordRepository

    @Autowired
    private lateinit var emotionRepository: EmotionRepository

    @Autowired
    private lateinit var descriptionRepository: DescriptionRepository

    @Test
    fun `데일리 감정 기록 API`() {
        // given
        val body = mapOf(
            "recordDate" to LocalDate.of(2021, 5, 5),
            "emotions" to mapOf("happy" to 10),
            "keywords" to setOf("study", "money"),
            "memo" to "first record"
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/records")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.recordId").isNotEmpty)
            .andDo(
                document(
                    "record/create",
                    requestHeaders(
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    requestFields(
                        fieldWithPath("recordDate").description("기록 날짜"),
                        subsectionWithPath("emotions").description("Key: 감정, Value: 갯수"),
                        fieldWithPath("keywords").description("감정 원인 키워드"),
                        fieldWithPath("memo").description("메모")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.recordId").description("기록 ID")
                    )
                )
            )
    }

    @Test
    fun `감정 기록 조회 API`() {
        // given
        val record = Record(
            userId = 1,
            recordDate = LocalDate.of(2021, 5, 5),
            keywords = setOf(Keyword.STUDY, Keyword.MONEY),
            memo = "first record"
        )
        recordRepository.save(record)
        emotionRepository.save(
            Emotion(
                record = record,
                emotionType = EmotionType.HAPPY,
                count = 10
            )
        )
        descriptionRepository.save(
            Description(
                minValue = 36,
                maxValue = 40,
                description = "롤러코스터같이 널뛰기하는 기분"
            )
        )

        // when
        val result = mockMvc.perform(
            get("/api/v1/records?recordDate=2021-05-05")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.userId").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("data.recordId").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("data.recordDate").value("2021-05-05"))
            .andExpect(MockMvcResultMatchers.jsonPath("data.memo").value("first record"))
            .andExpect(MockMvcResultMatchers.jsonPath("data.score").value("40"))
            .andExpect(MockMvcResultMatchers.jsonPath("data.description").value("롤러코스터같이 널뛰기하는 기분"))
            .andDo(
                document(
                    "record/find",
                    requestParameters(
                        parameterWithName("recordDate").description("조회 기록 날짜")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.userId").description("사용자 ID"),
                        fieldWithPath("data.recordId").description("기록 ID"),
                        fieldWithPath("data.recordDate").description("기록 날짜"),
                        fieldWithPath("data.emotions[].emotionType").description("감정"),
                        fieldWithPath("data.emotions[].count").description("갯수"),
                        fieldWithPath("data.keywords[]").description("감정 원인 키워드"),
                        fieldWithPath("data.memo").description("메모"),
                        fieldWithPath("data.score").description("점수"),
                        fieldWithPath("data.description").description("대표 감정")
                    )
                )
            )
    }
}
