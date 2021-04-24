package com.moa.exceptions

class NotEqualPasswordException(
    override val message: String = "비밀번호가 서로 다릅니다."
) : BadRequestException()
