package com.moa.auth.domain

import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider,
) {
    fun login(request: LoginRequest): String {
         return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        ).let { authentication ->
            SecurityContextHolder.getContext().authentication = authentication
            tokenProvider.createToken(authentication)
        }
    }
}
