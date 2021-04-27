package com.moa.exceptions

class PasswordNotMatchedException(
    override val message: String = "비밀번호를 확인해주세요."
) : BadRequestException()
