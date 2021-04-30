package com.moa.exceptions

class RecordNotFoundException(
    override val message: String = "감정 기록이 존재하지 않습니다."
) : RuntimeException()
