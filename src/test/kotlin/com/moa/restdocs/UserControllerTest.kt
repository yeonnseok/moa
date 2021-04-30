package com.moa.restdocs

import com.moa.common.ResultType
import com.moa.restdocs.common.LoginUserControllerTest
import com.moa.user.domain.RoleType
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class UserControllerTest : LoginUserControllerTest() {

    @Test
    fun `로그인 유저 조회 API`() {
        // when
        val result = mockMvc.perform(
            get("/api/v1/users/me")
                .header("Authorization", "Bearer $token")
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("data.nickName").value("moa"))
            .andExpect(MockMvcResultMatchers.jsonPath("data.email").value("moa@com"))
            .andExpect(MockMvcResultMatchers.jsonPath("data.profileEmotion").value("HAPPY"))
            .andExpect(MockMvcResultMatchers.jsonPath("data.role").value(RoleType.ROLE_USER.name))
            .andExpect(MockMvcResultMatchers.jsonPath("data.onboardingFlag").value(false))
            .andDo(
                MockMvcRestDocumentation.document(
                    "users/me",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("인증 토큰")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.nickName").description("닉네임"),
                        fieldWithPath("data.email").description("이메일"),
                        fieldWithPath("data.profileEmotion").description("프로필 이모티콘"),
                        fieldWithPath("data.role").description("권한"),
                        fieldWithPath("data.onboardingFlag").description("온보딩 완료 여부")
                    )
                )
            )
    }

    @Test
    fun `로그인 유저 정보 수정 API`() {
        // given
        val body = mapOf(
            "nickName" to "moa_changed",
            "password" to "c123",
            "profileEmotion" to "angry",
            "onboardingFlag" to true
        )

        // when
        val result = mockMvc.perform(
            patch("/api/v1/users/me")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(
                MockMvcRestDocumentation.document(
                    "users/update",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("인증 토큰")
                    ),
                    requestFields(
                        fieldWithPath("nickName")
                            .optional()
                            .description("닉네임"),
                        fieldWithPath("password")
                            .optional()
                            .description("비밀번호"),
                        fieldWithPath("profileEmotion")
                            .optional()
                            .description("프로필 이모티콘"),
                        fieldWithPath("onboardingFlag")
                            .optional()
                            .description("온보딩 완료 여부"),
                    )
                )
            )
    }
}