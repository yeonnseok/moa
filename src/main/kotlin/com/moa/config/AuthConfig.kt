package com.moa.config

import com.moa.auth.controller.interceptor.AuthInterceptor
import com.moa.auth.controller.resolver.LoginUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfig(
    private val authInterceptor: AuthInterceptor,
    private val loginUserArgumentResolver: LoginUserArgumentResolver
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserArgumentResolver)
    }
}
