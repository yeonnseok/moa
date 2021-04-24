package com.moa.user.controller

import com.moa.common.ApiResponse
import com.moa.user.controller.request.SignupRequest
import com.moa.user.controller.response.SignupResponse
import com.moa.user.domain.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/auth/signup")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping
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
}