package com.moa.auth.domain

import com.moa.common.JwtTokenProvider
import com.moa.exceptions.EmailDuplicatedException
import com.moa.exceptions.PasswordNotEqualException
import com.moa.exceptions.PasswordNotMatchedException
import com.moa.exceptions.UserNotFoundException
import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.controller.request.SignupRequest
import com.moa.common.hashed
import com.moa.user.domain.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun signup(request: SignupRequest): Long {
        validateUserEmail(request.email)
        validatePassword(request.password, request.password2)

        val user = userRepository.save(request.toEntity())

        return user.id!!
    }

    private fun validateUserEmail(email: String) {
        val existUser = userRepository.findByEmail(email)
        if (existUser != null) {
            throw EmailDuplicatedException()
        }
    }

    private fun validatePassword(password: String, password2: String) {
        if (password != password2) {
            throw PasswordNotEqualException()
        }
    }

    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException()

        checkPassword(request.password, user.password)
        return jwtTokenProvider.createToken(user.id!!)
    }

    private fun checkPassword(password: String, hashed: String) {
        if (password.hashed() != hashed) {
            throw PasswordNotMatchedException()
        }
    }
}
