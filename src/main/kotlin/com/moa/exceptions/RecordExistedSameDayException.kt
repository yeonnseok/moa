package com.moa.exceptions

class RecordExistedSameDayException(
    override val message: String = "해당 일에 기록이 이미 존재합니다."
): RuntimeException()
