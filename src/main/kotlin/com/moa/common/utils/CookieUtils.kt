package com.moa.common.utils

import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CookieUtils {
    companion object {
        fun getCookie(request: HttpServletRequest, name: String): Cookie? {
            val cookies = request.cookies
            if (cookies != null && cookies.size > 0) {
                for (cookie in cookies) {
                    if (cookie.name == name) {
                        return cookie
                    }
                }
            }
            return null
        }

        fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
            val cookie = Cookie(name, value)
            cookie.path = "/"
            cookie.isHttpOnly = true
            cookie.maxAge = maxAge
            response.addCookie(cookie)
        }

        fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
            val cookies = request.cookies
            if (cookies != null && cookies.size > 0) {
                for (cookie in cookies) {
                    if (cookie.name == name) {
                        cookie.value = ""
                        cookie.path = "/"
                        cookie.maxAge = 0
                        response.addCookie(cookie)
                    }
                }
            }
        }

        fun serialize(data: Any): String {
            return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(data))
        }

        fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
            return cls.cast(
                SerializationUtils.deserialize(
                    Base64.getUrlDecoder().decode(cookie.value)
                )
            )
        }
    }
}