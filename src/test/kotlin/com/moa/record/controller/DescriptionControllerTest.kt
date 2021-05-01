package com.moa.record.controller

import com.moa.common.ResultType
import com.moa.record.domain.Description
import com.moa.record.domain.DescriptionRepository
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DescriptionControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var descriptionRepository: DescriptionRepository

    @Test
    fun `대표 감정 API`() {
        // given
        descriptionRepository.save(
            Description(
                minValue = 36,
                maxValue = 40,
                description = "롤러코스터같이 널뛰기하는 기분"
            )
        )

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/descriptions?score=40")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.description").value("롤러코스터같이 널뛰기하는 기분"))
            .andDo(
                document(
                    "description/find",
                    requestParameters(
                        parameterWithName("score").description("온도 점수"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.description").description("대표 감정")
                    )
                )
            )
    }
}
