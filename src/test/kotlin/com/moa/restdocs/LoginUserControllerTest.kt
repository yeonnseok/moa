package com.moa.restdocs

import com.moa.auth.controller.response.TokenResponse
import com.moa.common.ApiResponse
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class LoginUserControllerTest : ControllerTest() {

    protected var token: String? = null

    protected var userId: Long? = null

    @BeforeEach
    fun setUp() {
        val user = dataLoader.sample_user()
        userId = user.id

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
