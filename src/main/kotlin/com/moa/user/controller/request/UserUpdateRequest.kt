package com.moa.user.controller.request

data class UserUpdateRequest(
    val username: String?,
    val password: String?,
    val image: String?
) {
}