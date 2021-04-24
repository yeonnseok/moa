package com.moa.user.domain

import com.moa.exceptions.NotEqualPasswordException
import com.moa.user.controller.request.SignupRequest
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class AuthServiceTest {

    @Autowired
    private lateinit var sut: AuthService

    @Test
    fun `회원가입에 실패한다`() {
        // given
        val request = SignupRequest("moa", "moa@com", "m123", "m111")

        // when
        shouldThrow<NotEqualPasswordException> { sut.signup(request) }
    }

    @Test
    fun `회원가입에 성공한다`() {
        // given
        val request = SignupRequest("moa", "moa@com", "m123", "m123")

        // when
        val userId = sut.signup(request)

        // given
        userId shouldBe 1
    }
}