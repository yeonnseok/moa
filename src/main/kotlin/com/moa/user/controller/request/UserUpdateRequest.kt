package com.moa.user.controller.request

data class UserUpdateRequest(
    val nickName: String?,
    val password: String?,
    val profileEmotion: String?,
    val onboardingFlag: Boolean? = null
)
