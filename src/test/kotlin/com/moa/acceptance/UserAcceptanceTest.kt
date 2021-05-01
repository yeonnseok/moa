package com.moa.acceptance

import com.moa.user.controller.response.UserResponse
import com.moa.common.ResultType
import com.moa.user.controller.request.UserUpdateRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.http.HttpStatus
import java.util.stream.Stream

class UserAcceptanceTest : AcceptanceTest() {

    @DisplayName("사용자 프로필/설정 인수테스트")
    @TestFactory
    fun auth(): Stream<DynamicTest> {
        return Stream.of(
            dynamicTest("로그인 유저 조회", {
                // when
                val response = get("/api/v1/users/me")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()

                val userResponse = getResponseData(response.data, UserResponse::class.java) as UserResponse

                userResponse.nickName shouldBe null
                userResponse.email shouldBe "moa@com"
            }),

            dynamicTest("로그인 유저 정보 수정", {
                // given
                val request = UserUpdateRequest(
                    nickName = "changed username",
                    password = "changed pw",
                    profileEmotion = "angry"
                )

                // when
                patch("/api/v1/users/me", request)
            })
        )
    }
}
