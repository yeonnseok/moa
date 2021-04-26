package com.moa.auth.domain

import com.moa.common.hash
import com.moa.exceptions.EmailDuplicatedException
import com.moa.exceptions.PasswordNotEqualException
import com.moa.exceptions.PasswordNotMatchedException
import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.controller.request.SignupRequest
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class AuthServiceTest {

    @Autowired
    private lateinit var sut: AuthService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `회원가입에 실패한다 - 비밀번호 확인`() {
        // given
        val request = SignupRequest("moa", "moa@com", "m123", "m111")

        // when
        val exception = shouldThrow<PasswordNotEqualException> { sut.signup(request) }

        // then
        exception.message shouldBe "비밀번호가 서로 다릅니다."
    }

    @Test
    fun `회원가입에 실패한다 - 중복 email`() {
        // given
        userRepository.save(
            User(
                username = "moa",
                email = "moa@com",
                password = hash("m123")
            )
        )
        val request = SignupRequest("moa", "moa@com", "m123", "m123")

        // when
        val exception = shouldThrow<EmailDuplicatedException> { sut.signup(request) }

        // then
        exception.message shouldBe "이미 존재하는 email 입니다."
    }

    @Test
    fun `회원가입에 성공한다`() {
        // given
        val request = SignupRequest("moa", "moa@com", "m123", "m123")

        // when
        val userId = sut.signup(request)

        // given
        userId shouldNotBe 1
    }

    @Test
    fun `로그인에 실패한다`() {
        // given
        userRepository.save(
            User(
                username = "moa",
                email = "moa@com",
                password = hash("m123")
            )
        )
        val request = LoginRequest("moa@com", "m111")

        // when
        val exception = shouldThrow<PasswordNotMatchedException> { sut.login(request) }

        // then
        exception.message shouldBe "비밀번호를 확인해주세요."
    }

    @Test
    fun `로그인에 성공한다`() {
        // given
        userRepository.save(
            User(
                username = "moa",
                email = "moa@com",
                password = hash("m123")
            )
        )
        val request = LoginRequest("moa@com", "m123")

        // when
        val token = sut.login(request)

        // then
        token shouldNotBe null
    }
}
