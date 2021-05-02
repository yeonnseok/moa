package com.moa.exceptions

class RecommendationNotFoundException(
    override val message: String = "존재하지 않는 추천 컨텐츠 입니다."
): RuntimeException()
