package com.moa.record.domain

enum class EmotionType(
    val score: Int
) {
    HAPPY(4),
    FLUTTER(3),
    PROUD(2),
    NERVOUS(-1),
    SAD(-2),
    ANNOY(-3),
    ANGRY(-4);

    companion object {
        fun of(emotion: String): EmotionType =
            values()
                .filter { it.name.equals(emotion, ignoreCase = true) }
                .first()
    }
}
