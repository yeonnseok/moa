package com.moa.common

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
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
}
