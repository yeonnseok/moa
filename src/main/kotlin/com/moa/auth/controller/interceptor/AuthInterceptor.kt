package com.moa.auth.controller.interceptor

import com.moa.auth.controller.utils.AuthorizationExtractor
import com.moa.auth.controller.utils.InterceptorValidator
import com.moa.common.JwtTokenProvider
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(
    private val authorizationExtractor: AuthorizationExtractor,
    private val jwtTokenProvider: JwtTokenProvider,
    private val interceptorValidator: InterceptorValidator
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (interceptorValidator.isNotValid(handler)) {
            return true
        }

        val token = authorizationExtractor.extract(request, "bearer")
        val claims = jwtTokenProvider.extractValidSubject(token)
        val userId = claims["userId"].toString()

        request.setAttribute("userId", userId)
        return true
    }
}