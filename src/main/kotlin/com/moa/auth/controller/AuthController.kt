package com.moa.auth.controller

import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.controller.response.TokenResponse
import com.moa.auth.controller.response.UserCreateResponse
import com.moa.common.ApiResponse
import com.moa.auth.controller.request.SignupRequest
import com.moa.auth.security.JwtTokenProvider
import com.moa.user.domain.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): ResponseEntity<ApiResponse> {
        val userId = userService.create(request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/user/me")
            .buildAndExpand(userId).toUri()

        return ResponseEntity
            .created(location)
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = UserCreateResponse(userId)
                )
            )
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse> {
        val token = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        ).let { authentication ->
            SecurityContextHolder.getContext().authentication = authentication
            jwtTokenProvider.createToken(authentication)
        }

        return ResponseEntity
            .ok(ApiResponse(data = TokenResponse(token)))
    }
}
