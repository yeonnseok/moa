package com.moa.bookmark.controller

import com.moa.common.ResultType
import com.moa.recommendation.domain.*
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class BookmarkControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var recommendationRepository: RecommendationRepository

    @Autowired
    private lateinit var contentRepository: ContentRepository

    @Test
    fun `북마크 생성 API`() {
        // given
        val content = contentRepository.save(
            Content(
                title = "소울",
                contents = """
                    음악을 가르치는 조는 학생들에게 음악이 무엇인지 재즈가 무엇인지를 가르칩니다. 조는 재즈 연주가가 되는 것이 인생의 최대 목표입니다. 하지만 원하던 기회가 찾아왔을 때, 맨홀에 빠지게 되며 죽음의 세계로 들어가게 됩니다.

                    이 영화는 모든 사람에게 삶의 목적이 필요한 것인지 다시 되돌아보게 합니다. 단순하지만 묵직한 울림을 계속 던져주는 영화입니다.

                    애니메이션을 넘어서 많은 생각과, 고민 또 그간의 쌓여온 감정들을 자세히 되돌아보게 되는 질문이 가득한 영화였습니다.
                """.trimIndent(),
                minValue = 10,
                maxValue = 20,
                type = ContentType.MOVIE
            )
        )
        val recommendation = recommendationRepository.save(
            Recommendation(
                userId = userId!!,
                recordId = 1,
                content = content
            )
        )
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
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.bookmarkId").isNotEmpty)
            .andDo(
                MockMvcRestDocumentation.document(
                    "bookmark/create",
                    requestHeaders(
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    requestFields(
                        fieldWithPath("recommendationId").description("추천 컨텐츠 Id")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.bookmarkId").description("북마크 ID"),
                    )
                )
            )
    }

}