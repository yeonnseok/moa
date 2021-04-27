package com.moa.user.controller.request

import com.moa.user.domain.RoleType
import com.moa.user.domain.User

data class UserCreateRequest(
    val username: String,
    val email: String,
    val password: String,
    val password2: String,
    val role: String,
    val image: String? = null
) {
    fun toEntity(encodedPw: String): User {
        return User(
            username = username,
            email = email,
            password = encodedPw,
            role = RoleType.valueOf(role),
            image = image
        )
    }
}
