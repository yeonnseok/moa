package com.moa.auth.controller.request

import com.moa.record.domain.EmotionType
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignupRequest(
    @NotBlank
    val nickName: String,

    @NotBlank
    @Email
    val email: String,

    @NotBlank
    val password: String,

    @NotBlank
    val password2: String,

    val role: String = RoleType.ROLE_USER.name,

    val profileEmotion: String
) {
    fun toEntity(encodedPw: String): User {
        return User(
            nickName = nickName,
            email = email,
            password = encodedPw,
            role = RoleType.valueOf(role),
            profileEmotionType = EmotionType.of(profileEmotion)
        )
    }
}
