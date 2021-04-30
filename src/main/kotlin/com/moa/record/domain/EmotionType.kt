package com.moa.record.domain

enum class EmotionType(
    val score: Int
) {
    HAPPY(4),
    EXCITED(3),
    PROUD(2),
    ANXIOUS(-1),
    DEPRESSED(-2),
    ANNOYING(-3),
    ANGRY(-4);

    companion object {
        fun of(emotion: String): EmotionType =
            values()
                .filter { it.name.equals(emotion, ignoreCase = true) }
                .first()
    }
}
