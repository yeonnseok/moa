package com.moa.auth.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        authException.printStackTrace()
        log.error("Responding with unauthorized error. Message - {}", authException.message)
        response.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            authException.localizedMessage
        )
    }
}
