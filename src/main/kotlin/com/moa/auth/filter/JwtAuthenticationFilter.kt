package com.moa.auth.filter

import com.moa.auth.security.JwtTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
): GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        jwtTokenProvider.resolveToken((request as HttpServletRequest?)!!).let {
            when (it != null && jwtTokenProvider.validateToken(it)) {
                true -> {
                    SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(it)
                }
                false -> {}
            }
        }

        chain!!.doFilter(request, response)
    }
}