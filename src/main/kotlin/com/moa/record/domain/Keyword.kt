package com.moa.record.domain

enum class Keyword(
    val text: String
) {
    FAMILY("가족"),
    STUDY("공부"),
    FRIEND("관계"),
    MONEY("돈"),
    WORK("일"),
    ETC("기타");

    companion object {
        fun of(keyword: String): Keyword =
            values()
                .filter { it.name.equals(keyword, ignoreCase = true) }
                .first()
    }
}
