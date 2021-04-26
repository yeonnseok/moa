package com.moa.auth.controller.request

import com.moa.common.hashed
import com.moa.user.domain.User

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val password2: String,
    val image: String? = null
) {
    fun toEntity(): User {
        return User(
            username = username,
            email = email,
            password = password.hashed(),
            image = image
        )
    }
}
