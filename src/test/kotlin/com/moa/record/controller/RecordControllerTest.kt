package com.moa.record.controller

import com.moa.common.ResultType
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

class RecordControllerTest : LoginUserControllerTest() {

    @Test
    fun `데일리 감정 기록 생성 API`() {
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
            .andExpect(status().isCreated)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("data.recordId").isNotEmpty)
            .andDo(
                document(
                    "record/create",
                    requestHeaders(
                        headerWithName("Content-Type").description("전송 타입"),
                        headerWithName("Authorization").description("인증 토큰")
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
    fun `데일리 감정 기록 조회 API`() {
        // given
        dataLoader.sample_description_36_to_40()
        val record = dataLoader.sample_record_by(userId!!)
        dataLoader.sample_emotion_happy_by(record, 10)

        // when
        val result = mockMvc.perform(
            get("/api/v1/records?recordDate=2021-05-05")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.userId").isNotEmpty)
            .andExpect(jsonPath("data.recordId").isNotEmpty)
            .andExpect(jsonPath("data.recordDate").value("2021-05-05"))
            .andExpect(jsonPath("data.memo").value("first memo"))
            .andExpect(jsonPath("data.score").value("40"))
            .andExpect(jsonPath("data.description").value("롤러코스터같이 널뛰기하는 기분"))
            .andDo(
                document(
                    "record/daily",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
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
                        fieldWithPath("data.score").description("온도"),
                        fieldWithPath("data.description").description("대표 감정")
                    )
                )
            )
    }

    @Test
    fun `주간 감정 기록 조회 API`() {
        // given
        val record1 = dataLoader.sample_record_first_by(userId!!)
        val record2 = dataLoader.sample_record_second_by(userId!!)
        dataLoader.sample_emotion_happy_and_excited_by(record1, record2)
        dataLoader.sample_description_14_to_16()
        dataLoader.sample_description_36_to_40()

        // when
        val result = mockMvc.perform(
            get("/api/v1/records?fromDate=2021-05-01&toDate=2021-05-08")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.averageScore").value(8))
            .andExpect(jsonPath("data.empathyPercentage").value(100))
            .andDo(
                document(
                    "record/weekly",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    requestParameters(
                        parameterWithName("fromDate").description("시작 날짜"),
                        parameterWithName("toDate").description("끝 날짜")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.averageScore").description("평균 온도"),
                        fieldWithPath("data.empathyPercentage").description("공감 퍼센트"),
                        fieldWithPath("data.records[].recordId").description("기록 ID"),
                        fieldWithPath("data.records[].recordDate").description("기록 날짜"),
                        fieldWithPath("data.records[].score").description("온도"),
                    )
                )
            )
    }

    @Test
    fun `데일리 감정 기록 수정 API`() {
        // given
        dataLoader.sample_description_36_to_40()
        val record = dataLoader.sample_record_by(userId!!)
        dataLoader.sample_emotion_happy_by(record, 10)

        val body = mapOf(
                "memo" to "change memo"
        )

        // when
        val result = mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/v1/records/{id}", record.id)
                        .header("Authorization", "Bearer $token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("data.userId").isNotEmpty)
                .andExpect(jsonPath("data.recordId").isNotEmpty)
                .andExpect(jsonPath("data.recordDate").value("2021-05-05"))
                .andExpect(jsonPath("data.memo").value("change memo"))
                .andExpect(jsonPath("data.score").value("40"))
                .andExpect(jsonPath("data.description").value("롤러코스터같이 널뛰기하는 기분"))
                .andDo(
                        document(
                                "record/update",
                                requestHeaders(
                                        headerWithName("Content-Type").description("전송 타입"),
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("id").description("감정 기록 ID")
                                ),
                                requestFields(
                                        fieldWithPath("memo").description("메모")
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
                                        fieldWithPath("data.score").description("온도"),
                                        fieldWithPath("data.description").description("대표 감정")
                                )
                        )
                )
    }

    @Test
    fun `데일리 감정 기록 삭제 API`() {
        // given
        dataLoader.sample_description_36_to_40()
        val record = dataLoader.sample_record_by(userId!!)
        dataLoader.sample_emotion_happy_by(record, 10)

        // when
        val result = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/v1/records/{id}", record.id)
                        .header("Authorization", "Bearer $token")
        )

        // then
        result
                .andExpect(status().isNoContent)
                .andDo(
                        document(
                                "record/delete",
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("id").description("감정 기록 ID")
                                )
                        )
                )
    }
}
