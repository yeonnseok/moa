package com.moa.user.controller

import com.moa.common.ResultType
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


internal class AuthControllerTest: ControllerTest() {

    @Test
    fun `회원 가입을 한다`() {
        // given
        val body = mapOf(
            "username" to "moa",
            "email" to "moa@com",
            "password" to "m123",
            "password2" to "m123",
            "image" to "image-url"
        )

        // when
        val result = this.mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("result").value(ResultType.SUCCESS.name))
            .andExpect(jsonPath("statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("data.userId").value(1))
            .andDo(
                document(
                    "auth/signup",
                    requestFields(
                        fieldWithPath("username").description("닉네임"),
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("비밀번호"),
                        fieldWithPath("password2").description("비밀번호 확인"),
                        fieldWithPath("image").description("이미지 경로")
                    ),
                    responseFields(
                        fieldWithPath("result").description("요청 결과"),
                        fieldWithPath("statusCode").description("응답 상태 코드"),
                        fieldWithPath("data.userId").description("User Id")
                    )
                )
            )
    }
}