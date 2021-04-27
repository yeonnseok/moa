package com.moa.exceptions

class AccessTokenLoadException(
    override val message: String = "토큰을 Load하지 못했습니다."
) : UnAuthorizedException()
