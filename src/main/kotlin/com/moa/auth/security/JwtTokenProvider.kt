package com.moa.auth.security

import com.moa.auth.domain.CustomUserDetailsService
import com.moa.auth.domain.UserPrincipal
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.token.secret-key:sample}")
    val secretKey: String,

    @Value("\${security.jwt.token.expire-length:300000}")
    val validityInMilliseconds: Long,

    private val userDetailsService: CustomUserDetailsService
) {
    fun createToken(authentication: Authentication?): String =
        Jwts.builder().let {
            val now = Date()

            val userPrincipal = authentication?.principal as UserPrincipal

            it.setClaims(
                Jwts.claims().setSubject(userPrincipal.username)
                    .also { claims ->
                        claims["role"] = userPrincipal.authorities.first()
                    }
            )
                .setIssuedAt(now)
                .setExpiration(Date(now.time + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
        }!!

    fun getAuthentication(token: String?): Authentication =
        userDetailsService.loadUserByUsername(this.getUserEmail(token)).let {
            UsernamePasswordAuthenticationToken(it, it.password, it.authorities)
        }

    fun getUserEmail(token: String?): String =
        Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .subject

    fun resolveToken(request: HttpServletRequest) =
        request.getHeader("Authorization")?.let {
            when (it.startsWith("Bearer ")) {
                true -> it.substring(7, it.length)
                false -> null
            }
        }

    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .let { !it.body.expiration.before(Date()) }
        } catch (e: Exception) {
            false
        }
    }
}
