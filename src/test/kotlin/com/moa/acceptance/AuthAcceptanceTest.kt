package com.moa.acceptance

import com.moa.common.ResultType
import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.controller.request.SignupRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.http.HttpStatus
import java.util.stream.Stream

internal class AuthAcceptanceTest : AcceptanceTest() {

    @DisplayName("사용자 회원가입/로그인 인수테스트")
    @TestFactory
    fun auth(): Stream<DynamicTest> {
        return Stream.of(
            dynamicTest("회원 가입", {
                // given
                val request = SignupRequest("moa", "auth@com", "m123", "m123")

                // when
                val response = post("/api/auth/signup", request)

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.CREATED.value()
            }),

            dynamicTest("로그인", {
                // given
                val request = LoginRequest("auth@com", "m123")

                // when
                val response = login("/api/auth/login", request)

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()
            })
        )
    }
}
