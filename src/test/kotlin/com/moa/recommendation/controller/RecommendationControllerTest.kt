package com.moa.recommendation.controller

import com.moa.common.ResultType
import com.moa.recommendation.domain.ContentType
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RecommendationControllerTest : LoginUserControllerTest() {

    @Test
    fun `온도 기반 컨텐츠 추천 API`() {
        // given
        val record = dataLoader.sample_record_by(userId!!)
        dataLoader.sample_emotion_happy_by(record, 4)
        dataLoader.sample_description_14_to_16()
        val content = dataLoader.sample_content_soul()

        // when
        val result = mockMvc.perform(
            get("/api/v1/recommendations?recordId=${record.id}")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.recommendationId").isNotEmpty)
            .andExpect(jsonPath("data.title").value(content.title))
            .andExpect(jsonPath("data.contents").value(content.contents))
            .andExpect(jsonPath("data.type").value(ContentType.MOVIE.name))
            .andDo(
                document(
                    "recommendation/find",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    requestParameters(
                        parameterWithName("recordId").description("감정 기록 Id")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.contentId").description("컨텐츠 ID"),
                        fieldWithPath("data.description").description("대표 기분"),
                        fieldWithPath("data.recommendationId").description("추천 컨텐츠 ID"),
                        fieldWithPath("data.title").description("제목"),
                        fieldWithPath("data.contents").description("내용"),
                        fieldWithPath("data.type").description("컨텐츠 타입"),
                        fieldWithPath("data.bookmarkId").description("북마크 ID")
                    )
                )
            )
    }
}