package com.moa.user.domain

import com.moa.auth.controller.request.SignupRequest
import com.moa.exceptions.EmailDuplicatedException
import com.moa.exceptions.PasswordNotEqualException
import com.moa.user.controller.request.UserUpdateRequest
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserServiceTest {

    @Autowired
    private lateinit var sut: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `유저 생성 실패 - 비밀번호 확인`() {
        // given
        val request = SignupRequest(
            nickName = "moa",
            email = "moa@com",
            password = "m123",
            password2 = "m111",
            role = "ROLE_USER"
        )

        // when
        val exception = shouldThrow<PasswordNotEqualException> { sut.create(request) }

        // then
        exception.message shouldBe "비밀번호가 서로 다릅니다."
    }

    @Test
    fun `유저 생성 실패 - 중복 email`() {
        // given
        userRepository.save(
            User(
                nickName = "moa",
                email = "moa@com",
                password = passwordEncoder.encode("m123"),
                role = RoleType.ROLE_USER
            )
        )

        val request = SignupRequest(
            nickName = "moa",
            email = "moa@com",
            password = "m123",
            password2 = "m123",
            role = "ROLE_USER"
        )

        // when
        val exception = shouldThrow<EmailDuplicatedException> { sut.create(request) }

        // then
        exception.message shouldBe "이미 존재하는 email 입니다."
    }

    @Test
    fun `회원가입에 성공한다`() {
        // given
        val request = SignupRequest(
            nickName = "moa",
            email = "moa@com",
            password = "m123",
            password2 = "m123",
            role = "ROLE_USER"
        )

        // when
        val userId = sut.create(request)

        // then
        userId shouldNotBe null
    }

    @Test
    fun `유저 조회`() {
        // given
        val savedUser = userRepository.save(
            User(
                nickName = "moa",
                email = "moa@com",
                password = passwordEncoder.encode("m123"),
                role = RoleType.ROLE_USER
            )
        )

        // when
        val findUser = sut.find(savedUser.id!!)

        // then
        findUser shouldBe savedUser
    }

    @Test
    fun `유저 수정`() {
        // given
        val savedUser = userRepository.save(
            User(
                nickName = "moa",
                email = "moa@com",
                password = passwordEncoder.encode("m123"),
                role = RoleType.ROLE_USER
            )
        )

        // when
        val user = sut.update(
            savedUser.id!!,
            UserUpdateRequest(
                nickName = "changed username",
                password = "change pw",
                profileEmotion = "changed url"
            )
        )

        // then
        val findUser = sut.find(user.id!!)
        findUser.nickName shouldBe "changed username"
        passwordEncoder.matches("change pw", findUser.password) shouldBe true
        findUser.imageUrl shouldBe "changed url"
    }
}
