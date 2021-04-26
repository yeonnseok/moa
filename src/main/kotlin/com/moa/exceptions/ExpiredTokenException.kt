package com.moa.exceptions

class ExpiredTokenException(
    override val message: String = "토큰의 유효 기간이 만료되었습니다."
) : UnAuthorizedException()
