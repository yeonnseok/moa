package com.moa.exceptions

class ContentNotFoundException(
    override val message: String = "저장된 컨텐츠가 없습니다."
) : RuntimeException()
