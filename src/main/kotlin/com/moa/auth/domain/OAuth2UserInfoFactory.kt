package com.moa.auth.domain

import com.moa.auth.domain.dto.GoogleOAuth2UserInfo
import com.moa.auth.domain.dto.OAuth2UserInfo
import com.moa.exceptions.OAuth2AuthenticationProcessingException
import com.moa.user.domain.AuthProvider
import org.slf4j.LoggerFactory

class OAuth2UserInfoFactory {

    companion object {
        private val log = LoggerFactory.getLogger(javaClass)

        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>) : OAuth2UserInfo {
            log.info("provider: $registrationId | attributes : $attributes")
            if(registrationId.equals(AuthProvider.GOOGLE.name, ignoreCase = true)) {
                return GoogleOAuth2UserInfo(attributes);
            } else {
                throw OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
            }
        }
    }
}
