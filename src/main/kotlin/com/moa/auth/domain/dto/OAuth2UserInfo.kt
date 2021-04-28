package com.moa.auth.domain.dto

abstract class OAuth2UserInfo(
    protected open val attributes: Map<String, Any>
) {
    abstract fun getOAuthId() : String

    abstract fun getNickName() : String

    abstract fun getEmail() : String
}
