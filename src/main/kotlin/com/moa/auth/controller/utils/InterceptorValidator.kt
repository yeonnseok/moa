package com.moa.auth.controller.utils

import com.moa.auth.controller.interceptor.NoValidate
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import java.util.*

@Component
class InterceptorValidator {

    fun isNotValid(handler: Any): Boolean {
        val noValidate = (handler as HandlerMethod).getMethodAnnotation(NoValidate::class.java)
        return Objects.nonNull(noValidate)
    }
}