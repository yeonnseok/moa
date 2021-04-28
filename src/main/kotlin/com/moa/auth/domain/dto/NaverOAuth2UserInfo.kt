package com.moa.auth.domain.dto

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NaverOAuth2UserInfo(
    override val attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {

    private val mapType = object : TypeToken<Map<String, Any>>() {}.type
    private val response: Map<String, Any> = Gson().fromJson(attributes.get("response").toString(), mapType)

    override fun getId() = response.get("id").toString()

    override fun getName() = response.get("nickname").toString()

    override fun getEmail() = response.get("email").toString()
}
