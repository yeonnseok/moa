package com.moa.user.domain

import com.moa.common.hashed
import com.moa.exceptions.UserNotFoundException
import com.moa.user.controller.request.UserUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
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
            user.password = request.password.hashed()
        }

        if (request.image != null) {
            user.image = request.image
        }

        return user
    }
}