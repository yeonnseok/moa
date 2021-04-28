package com.moa.auth.domain

import com.moa.auth.domain.dto.GoogleOAuth2UserInfo
import com.moa.auth.domain.dto.KakaoOAuth2UserInfo
import com.moa.auth.domain.dto.NaverOAuth2UserInfo
import com.moa.auth.domain.dto.OAuth2UserInfo
import com.moa.exceptions.OAuth2AuthenticationProcessingException
import com.moa.user.domain.AuthProvider

class OAuth2UserInfoFactory {
    companion object {

        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            return when {
                AuthProvider.GOOGLE.equalWith(registrationId) -> {
                    GoogleOAuth2UserInfo(attributes)
                }
                AuthProvider.KAKAO.equalWith(registrationId) -> {
                    KakaoOAuth2UserInfo(attributes)
                }
                AuthProvider.NAVER.equalWith(registrationId) -> {
                    NaverOAuth2UserInfo(attributes)
                }
                else -> {
                    throw OAuth2AuthenticationProcessingException("$registrationId 로그인은 지원하지 않습니다.");
                }
            }
        }
    }
}
