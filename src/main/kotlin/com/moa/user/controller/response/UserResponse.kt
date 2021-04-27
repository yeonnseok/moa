package com.moa.user.controller.response

import com.moa.user.domain.RoleType
import com.moa.user.domain.User

data class UserResponse(
    val username: String,
    val email: String,
    val image: String?,
    val role: RoleType
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                username = user.username,
                email = user.email,
                image = user.image,
                role = user.role
            )
        }
    }
}
