package com.moa.exceptions

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException(
    override val message: String = "OAuth2 인증 오류!!"
) : AuthenticationException(message)


