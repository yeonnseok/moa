package com.moa.auth.controller.resolver

import com.moa.user.domain.User
import com.moa.user.domain.UserService
import lombok.RequiredArgsConstructor
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
@RequiredArgsConstructor
class LoginUserArgumentResolver(
    private val userService: UserService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): User {
        val userId = webRequest.getAttribute("userId", RequestAttributes.SCOPE_REQUEST) as String?
        return userService.find(userId!!.toLong())
    }
}
