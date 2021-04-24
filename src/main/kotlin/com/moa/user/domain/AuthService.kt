package com.moa.user.domain

import com.moa.exceptions.NotEqualPasswordException
import com.moa.user.controller.request.SignupRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun signup(request: SignupRequest): Long {
        log.info("${request.password} - ${request.password2}")
        validatePassword(request.password, request.password2)

        val user = userRepository.save(request.toEntity())

        return user.id!!
    }

    private fun validatePassword(password: String, password2: String) {
        log.info("${password} - ${password2}")
        if (password != password2) {
            throw NotEqualPasswordException()
        }
    }
}
