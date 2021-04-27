package com.moa.user.domain

import com.moa.user.controller.request.UserCreateRequest
import com.moa.exceptions.EmailDuplicatedException
import com.moa.exceptions.PasswordNotEqualException
import com.moa.exceptions.UserNotFoundException
import com.moa.user.controller.request.UserUpdateRequest
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(request: UserCreateRequest): Long {
        validateUserEmail(request.email)
        validatePassword(request.password, request.password2)

        val encodedPw = passwordEncoder.encode(request.password)
        val user = userRepository.save(request.toEntity(encodedPw))

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

    fun find(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { UserNotFoundException() }
    }

    @Transactional
    fun update(id: Long, request: UserUpdateRequest): User {
        val user = find(id)

        if (request.username != null) {
            user.username = request.username
        }
        if (request.password != null) {
            user.password = passwordEncoder.encode(request.password)
        }
        if (request.image != null) {
            user.image = request.image
        }

        return user
    }
}
