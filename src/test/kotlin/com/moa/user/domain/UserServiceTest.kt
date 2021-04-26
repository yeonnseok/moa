package com.moa.user.domain

import com.moa.common.hashed
import com.moa.user.controller.request.UserUpdateRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserServiceTest {

    @Autowired
    private lateinit var sut: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `유저 조회`() {
        // given
        val savedUser = userRepository.save(
            User(
                username = "moa",
                email = "moa@com",
                password = "m123".hashed()
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
                username = "moa",
                email = "moa@com",
                password = "m123".hashed()
            )
        )

        // when
        sut.update(
            savedUser.id!!,
            UserUpdateRequest(
                username = "changed username",
                password = "change pw",
                image = "changed url"
            )
        )

        // then
        val findUser = sut.find(savedUser.id!!)
        findUser.username shouldBe "changed username"
        findUser.password shouldBe "change pw".hashed()
        findUser.image shouldBe "changed url"
    }
}
