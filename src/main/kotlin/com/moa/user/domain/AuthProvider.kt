package com.moa.user.domain

enum class AuthProvider {
    LOCAL,
    GOOGLE,
    KAKAO,
    NAVER;

    companion object {
        fun of(provider: String): AuthProvider =
            values()
                .filter { it.name.equals(provider, ignoreCase = true) }
                .first()
    }

    fun equalWith(provider: String) =
        provider.equals(this.name, ignoreCase = true)
}
