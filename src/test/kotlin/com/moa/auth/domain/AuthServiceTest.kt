package com.moa.auth.domain

import com.moa.auth.controller.request.LoginRequest
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class AuthServiceTest {

    @Autowired
    private lateinit var sut: AuthService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `로그인에 실패한다`() {
        // given
        userRepository.save(
            User(
                username = "moa",
                email = "moa@com",
                password = passwordEncoder.encode("m123"),
                role = RoleType.ROLE_USER
            )
        )
        val request = LoginRequest("moa@com", "m111")

        // when
        val exception = shouldThrow<BadCredentialsException> { sut.login(request) }
    }

    @Test
    fun `로그인에 성공한다`() {
        // given
        userRepository.save(
            User(
                username = "moa",
                email = "moa@com",
                password = passwordEncoder.encode("m123"),
                role = RoleType.ROLE_USER
            )
        )
        val request = LoginRequest("moa@com", "m123")

        // when
        val token = sut.login(request)

        // then
        token shouldNotBe null
    }
}
