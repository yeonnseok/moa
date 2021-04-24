package com.moa.acceptance

import com.moa.common.ApiResponse
import com.moa.common.ResultType
import com.moa.user.controller.request.SignupRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

internal class AuthAcceptanceTest : AcceptanceTest() {

    @Test
    fun `회원가입에 실패한다 - 비밀번호 불일치`() {
        val request = SignupRequest("moa", "moa@com", "m123", "m111")

        val response = given().
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        `when`().
                post("/auth/signup").
        then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().`as`(ApiResponse::class.java)

        response.result shouldBe ResultType.FAIL
        response.statusCode shouldBe 400
    }

    @Test
    fun `회원가입에 성공한다`() {
        val request = SignupRequest("moa", "moa@com", "m123", "m123")

        given().
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        `when`().
                post("/auth/signup").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value())
    }
}
