package com.moa.user.domain

enum class Emotion {
    HAPPY,
    EXCITED,
    PROUD,
    ANXIOUS,
    DEPRESSED,
    ANNOYING,
    ANGRY;

    companion object {
        fun of(emotion: String): Emotion =
            values()
                .filter { it.name.equals(emotion, ignoreCase = true) }
                .first()
    }
}
