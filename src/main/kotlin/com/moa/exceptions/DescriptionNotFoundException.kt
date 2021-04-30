package com.moa.exceptions

class DescriptionNotFoundException(
    override val message: String = "저장된 대표 감정이 없습니다."
) : RuntimeException()
