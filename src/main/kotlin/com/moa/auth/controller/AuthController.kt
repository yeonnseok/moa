package com.moa.auth.controller

import com.moa.common.ApiResponse
import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.controller.request.SignupRequest
import com.moa.auth.controller.response.SignupResponse
import com.moa.auth.controller.response.TokenResponse
import com.moa.auth.domain.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signup")
    fun signup(@RequestBody request: SignupRequest): ResponseEntity<ApiResponse> {
        val userId = authService.signup(request)

        return ResponseEntity
            .created(URI("/users/${userId}"))
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = SignupResponse(userId)
                )
            )
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ApiResponse> {
        val token = authService.login(request)

        return ResponseEntity
            .ok(ApiResponse(data = TokenResponse(token)))
    }
}