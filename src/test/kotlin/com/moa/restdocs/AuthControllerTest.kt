package com.moa.restdocs

import com.moa.common.ResultType
import com.moa.record.domain.EmotionType
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTest : ControllerTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

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
            "nickName" to "moa",
            "email" to "moa@com",
            "password" to "m123",
            "password2" to "m123",
            "role" to RoleType.ROLE_USER.name,
            "profileEmotion" to "happy"
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
            .andExpect(jsonPath("data.userId").value(1))
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
                        fieldWithPath("role").description("권한")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("결과 코드"),
                        fieldWithPath("data.userId").description("유저 ID"))
                    )
                )
    }

    @Test
    fun `로그인 API`() {
        // given
        userRepository.save(
            User(
                nickName = "moa",
                email = "moa@com",
                password = passwordEncoder.encode("m123"),
                profileEmotionType = EmotionType.HAPPY,
                role = RoleType.ROLE_USER
            )
        )

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