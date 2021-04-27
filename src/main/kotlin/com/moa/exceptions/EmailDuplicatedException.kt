package com.moa.exceptions

class EmailDuplicatedException(
    override val message: String = "이미 존재하는 email 입니다."
) : BadRequestException()
