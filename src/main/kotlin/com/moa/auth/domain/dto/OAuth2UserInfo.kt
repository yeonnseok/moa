package com.moa.auth.domain.dto

abstract class OAuth2UserInfo(
    protected open val attributes: Map<String, Any>
) {
    abstract fun getId() : String

    abstract fun getName() : String

    abstract fun getEmail() : String
}
