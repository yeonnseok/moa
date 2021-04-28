package com.moa.auth.domain.dto

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moa.exceptions.OAuth2AuthenticationProcessingException


class KakaoOAuth2UserInfo(
    override val attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {

    private val mapType = object : TypeToken<Map<String, Any>>() {}.type

    override fun getOAuthId() = attributes.get("id").toString()

    override fun getNickName(): String {
        val properties: Map<String, Any> =
            Gson().fromJson(attributes.get("properties").toString(), mapType)

        return properties.get("nickname").toString()
    }

    override fun getEmail(): String {
        val accounts: Map<String, Any> =
            Gson().fromJson(attributes.get("kakao_account").toString(), mapType)

        return accounts.get("email")?.toString()
            ?: throw OAuth2AuthenticationProcessingException("이메일을 불러올 수 없습니다.")
    }
}
