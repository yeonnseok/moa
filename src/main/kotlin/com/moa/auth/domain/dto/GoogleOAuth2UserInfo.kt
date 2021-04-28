package com.moa.auth.domain.dto

class GoogleOAuth2UserInfo(
    override val attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {

    override fun getOAuthId() = attributes.get("sub").toString()

    override fun getNickName() = attributes.get("name").toString()

    override fun getEmail() = attributes.get("email").toString()
}
