package com.moa.user.controller.response

import com.moa.user.domain.RoleType
import com.moa.user.domain.User

data class UserResponse(
    val username: String,
    val email: String,
    val profileEmotion: String?,
    val role: RoleType,
    val onboardingFlag: Boolean
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                username = user.nickName,
                email = user.email,
                profileEmotion = user.profileEmotion?.name,
                role = user.role,
                onboardingFlag = user.onboardingFlag
            )
        }
    }
}
