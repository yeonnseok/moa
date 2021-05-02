package com.moa.exceptions

class BookmarkNotFoundException(
    override val message: String = "북마크가 존재하지 않습니다."
) : RuntimeException()
