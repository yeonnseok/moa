package com.moa.auth.controller.request

data class LoginRequest(
    val email: String,
    val password: String
)
