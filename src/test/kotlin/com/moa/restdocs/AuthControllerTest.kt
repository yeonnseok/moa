package com.moa.restdocs

import com.moa.common.ResultType
import com.moa.user.domain.AuthProvider
import com.moa.user.domain.RoleType
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTest : ControllerTest() {

    @Test
    fun `Email 확인 API`() {
        // given
        val body = mapOf(
            "email" to "moa@com"
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/auth/check")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.existed").value(false))
            .andDo(
                document(
                    "auth/email",
                    requestHeaders(
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    requestFields(
                        fieldWithPath("email").description("이메일"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.existed").description("이메일 중복 여부"))
                )
            )
    }

    @Test
    fun `회원가입 API`() {
        // given
        val body = mapOf(
            "email" to "moa@com",
            "password" to "m123",
            "password2" to "m123",
            "role" to RoleType.ROLE_USER.name,
            "authProvider" to AuthProvider.GOOGLE.name
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isCreated)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("data.token").isNotEmpty)
            .andDo(
                document(
                    "auth/signup",
                    requestHeaders(
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("비밀번호"),
                        fieldWithPath("password2").description("비밀번호 확인"),
                        fieldWithPath("role").description("권한"),
                        fieldWithPath("authProvider").description("Auth Provider")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.token").description("JWT 토큰"))
                    )
                )
    }

    @Test
    fun `로그인 API`() {
        // given
        dataLoader.sample_user()

        val body = mapOf(
            "email" to "moa@com",
            "password" to "m123"
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("data.token").isNotEmpty)
            .andDo(
                document(
                    "auth/login",
                    requestHeaders(
                        headerWithName("Content-Type").description("전송 타입")
                    ),
                    requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.token").description("JWT 토큰")
                    )
                )
            )
    }
}