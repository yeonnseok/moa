package com.moa.common

import com.moa.exceptions.AccessTokenLoadException
import com.moa.exceptions.ExpiredTokenException
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.token.secret-key:sample}")
    val secretKey: String,

    @Value("\${security.jwt.token.expire-length:300000}")
    val validityInMilliseconds: Long
) {
    fun createToken(userId: Long): String {
        val claims = Jwts.claims()
        claims.put("userId", userId.toString())

        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun extractValidSubject(token: String): Claims {
        validateToken(token)
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
    }

    private fun validateToken(token: String) {
        try {
            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)

            validateExpiredTime(claims)
        } catch (e: Exception) {
            e.multiCatch(JwtException::class, IllegalArgumentException::class) {
                throw AccessTokenLoadException()
            }
        }
    }

    private fun validateExpiredTime(claims: Jws<Claims>) {
        if (claims.body.expiration.before(Date())) {
            throw ExpiredTokenException()
        }
    }
}
