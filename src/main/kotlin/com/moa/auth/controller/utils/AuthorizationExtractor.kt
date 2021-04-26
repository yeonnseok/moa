package com.moa.auth.controller.utils

import org.apache.logging.log4j.util.Strings
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AuthorizationExtractor {

    private val authorization = "Authorization"
    private val accessTokenType = AuthorizationExtractor::class.java.simpleName + ".ACCESS_TOKEN_TYPE"

    fun extract(request: HttpServletRequest, type: String): String {
        val headers = request.getHeaders(authorization)

        while (headers.hasMoreElements()) {
            val value = headers.nextElement()
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                var authHeaderValue = value.substring(type.length).trim { it <= ' ' }
                request.setAttribute(
                    accessTokenType,
                    value.substring(0, type.length).trim { it <= ' ' })
                val commaIndex = authHeaderValue.indexOf(',')
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex)
                }
                return authHeaderValue
            }
        }
        return Strings.EMPTY
    }
}