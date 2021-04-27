package com.moa.user.domain

enum class AuthProvider {
    LOCAL,
    GOOGLE;

    companion object {
        fun of(name: String): AuthProvider =
            values()
                .filter { it.name.equals(name, ignoreCase = true) }
                .first()
    }
}
