package com.moa.restdocs.common

import com.moa.auth.controller.response.TokenResponse
import com.moa.common.ApiResponse
import com.moa.user.domain.Emotion
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class LoginUserControllerTest : ControllerTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    protected var token: String? = null

    @BeforeEach
    fun setUp() {
        userRepository.save(
            User(
                nickName = "moa",
                email = "moa@com",
                password = passwordEncoder.encode("m123"),
                profileEmotion = Emotion.HAPPY,
                role = RoleType.ROLE_USER
            )
        )

        val body = mapOf(
            "email" to "moa@com",
            "password" to "m123"
        )

        val result = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )
            .andReturn()

        val apiResponse = objectMapper.readValue(result.response.contentAsString, ApiResponse::class.java)
        val tokenResponse = objectMapper.readValue(objectMapper.writeValueAsString(apiResponse.data), TokenResponse::class.java)
        token = tokenResponse.token
    }
}
