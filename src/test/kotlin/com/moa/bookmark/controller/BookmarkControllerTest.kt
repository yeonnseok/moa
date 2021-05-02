package com.moa.bookmark.controller

import com.moa.common.ResultType
import com.moa.recommendation.domain.ContentType
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class BookmarkControllerTest : LoginUserControllerTest() {

    @Test
    fun `북마크 생성 API`() {
        // given
        val content = dataLoader.sample_content_soul()
        val recommendation = dataLoader.sample_recommendation_by(userId!!, content)
        val body = mapOf(
            "recommendationId" to recommendation.id!!
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/bookmarks")
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
            .andExpect(jsonPath("data.bookmarkId").isNotEmpty)
            .andDo(
                document(
                    "bookmark/create",
                    requestHeaders(
                        headerWithName("Content-Type").description("전송 타입"),
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    requestFields(
                        fieldWithPath("recommendationId").description("추천 컨텐츠 Id")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.bookmarkId").description("북마크 ID")
                    )
                )
            )
    }

    @Test
    fun `북마크 목록 조회 API`() {
        // given
        val record = dataLoader.sample_record_by(userId!!)
        val content = dataLoader.sample_content_soul()
        val recommendation = dataLoader.sample_recommendation_by(userId!!, record.id!!, content)
        dataLoader.sample_bookmark_by(recommendation)

        // when
        val result = mockMvc.perform(
            get("/api/v1/bookmarks")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andDo(
                document(
                    "bookmark/list",
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data[].recordId").description("기록 ID"),
                        fieldWithPath("data[].recordDate").description("기록 날짜"),
                        fieldWithPath("data[].score").description("온도"),
                        fieldWithPath("data[].recommendationId").description("추천 컨텐츠 Id"),
                        fieldWithPath("data[].title").description("제목"),
                        fieldWithPath("data[].contents").description("내용"),
                        fieldWithPath("data[].type").description("컨텐츠 타입")
                    )
                )
            )
    }

    @Test
    fun `북마크 상세 조회 API`() {
        // given
        val record = dataLoader.sample_record_by(userId!!)
        val content = dataLoader.sample_content_soul()
        val recommendation = dataLoader.sample_recommendation_by(userId!!, record.id!!, content)
        val bookmark = dataLoader.sample_bookmark_by(recommendation)

        // when
        val result = mockMvc.perform(
            get("/api/v1/bookmarks/{id}", bookmark.id)
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.recordId").value(record.id!!))
            .andExpect(jsonPath("data.recordDate").value(record.recordDate.toString()))
            .andExpect(jsonPath("data.score").value(record.totalScore()))
            .andExpect(jsonPath("data.recommendationId").value(recommendation.id!!))
            .andExpect(jsonPath("data.title").value(content.title))
            .andExpect(jsonPath("data.contents").value(content.contents))
            .andExpect(jsonPath("data.type").value(ContentType.MOVIE.name))
            .andDo(
                document(
                    "bookmark/detail",
                    pathParameters(
                        parameterWithName("id").description("북마크 Id")
                    ),
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.recordId").description("기록 ID"),
                        fieldWithPath("data.recordDate").description("기록 날짜"),
                        fieldWithPath("data.score").description("온도"),
                        fieldWithPath("data.recommendationId").description("추천 컨텐츠 Id"),
                        fieldWithPath("data.title").description("제목"),
                        fieldWithPath("data.contents").description("내용"),
                        fieldWithPath("data.type").description("컨텐츠 타입")
                    )
                )
            )
    }

    @Test
    fun `북마크 취소 API`() {
        // given
        val content = dataLoader.sample_content_soul()
        val recommendation = dataLoader.sample_recommendation_by(userId!!, content)
        val bookmark = dataLoader.sample_bookmark_by(recommendation)

        // when
        val result = mockMvc.perform(
            delete("/api/v1/bookmarks/{id}", bookmark.id)
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "bookmark/delete",
                    pathParameters(
                        parameterWithName("id").description("북마크 Id")
                    ),
                    requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                    )
                )
            )
    }
}
