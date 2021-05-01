package com.moa.auth.controller

import com.moa.auth.controller.request.EmailCheckRequest
import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.controller.request.SignupRequest
import com.moa.auth.controller.response.EmailCheckResponse
import com.moa.auth.controller.response.TokenResponse
import com.moa.auth.security.JwtTokenProvider
import com.moa.common.ApiResponse
import com.moa.user.domain.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService
) {
    @PostMapping("/check")
    fun check(@RequestBody request: EmailCheckRequest): ResponseEntity<ApiResponse> {
        val result = userService.checkEmail(request)
        return ResponseEntity
            .ok(ApiResponse(data = EmailCheckResponse(result)))
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): ResponseEntity<ApiResponse> {
        val userId = userService.create(request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/v1/user/me")
            .buildAndExpand(userId).toUri()

        val token = getToken(request.email, request.password)

        return ResponseEntity
            .created(location)
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = TokenResponse(token)
                )
            )
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse> {
        val token = getToken(request.email, request.password)

        return ResponseEntity
            .ok(ApiResponse(data = TokenResponse(token)))
    }

    private fun getToken(email: String, password: String) =
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, password)
        ).let { authentication ->
            SecurityContextHolder.getContext().authentication = authentication
            jwtTokenProvider.createToken(authentication)
        }
}
