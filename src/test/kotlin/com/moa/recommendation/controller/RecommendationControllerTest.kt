package com.moa.recommendation.controller

import com.moa.common.ResultType
import com.moa.recommendation.domain.Content
import com.moa.recommendation.domain.ContentRepository
import com.moa.recommendation.domain.ContentType
import com.moa.record.domain.*
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

class RecommendationControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var recordRepository: RecordRepository

    @Autowired
    private lateinit var emotionRepository: EmotionRepository

    @Autowired
    private lateinit var descriptionRepository: DescriptionRepository

    @Autowired
    private lateinit var contentRepository: ContentRepository

    @Test
    fun `온도 기반 컨텐츠 추천 API`() {
        // given
        val record = Record(
            userId = userId!!,
            recordDate = LocalDate.of(2021, 5, 5),
            keywords = setOf(Keyword.STUDY, Keyword.MONEY),
            memo = "first record"
        )
        recordRepository.save(record)
        emotionRepository.save(
            Emotion(
                record = record,
                emotionType = EmotionType.HAPPY,
                count = 4
            )
        )
        descriptionRepository.save(
            Description(
                minValue = 14,
                maxValue = 16,
                description = "산뜻하고 행복한 기분"
            ),
        )
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

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/recommendations?recordId=${record.id}&score=16")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.recommendationId").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("data.title").value(content.title))
            .andExpect(MockMvcResultMatchers.jsonPath("data.contents").value(content.contents))
            .andExpect(MockMvcResultMatchers.jsonPath("data.type").value(ContentType.MOVIE.name))
            .andDo(
                MockMvcRestDocumentation.document(
                    "recommendation/find",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("recordId").description("감정 기록 Id"),
                        RequestDocumentation.parameterWithName("score").description("온도")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("result").description("응답 결과"),
                        PayloadDocumentation.fieldWithPath("statusCode").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("data.recommendationId").description("추천 컨텐츠 ID"),
                        PayloadDocumentation.fieldWithPath("data.title").description("제목"),
                        PayloadDocumentation.fieldWithPath("data.contents").description("내용"),
                        PayloadDocumentation.fieldWithPath("data.type").description("컨텐츠 타입"),
                    )
                )
            )
    }
}