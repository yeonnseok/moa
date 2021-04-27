package com.moa.auth.domain

import com.moa.auth.domain.dto.OAuth2UserInfo
import com.moa.exceptions.OAuth2AuthenticationProcessingException
import com.moa.user.domain.*
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import javax.naming.AuthenticationException

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        log.info("userRequest: $oAuth2UserRequest")

        val oAuth2User = super.loadUser(oAuth2UserRequest)
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (e: AuthenticationException) {
            throw e
        } catch (e: Exception) {
            throw InternalAuthenticationServiceException(e.message, e.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.clientRegistration.registrationId,
            oAuth2User.attributes
        )

        if (oAuth2UserInfo.getEmail().isBlank()) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 Provider")
        }

        var user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
        if (user != null) {
            if (user.authProvider != AuthProvider.of(oAuth2UserRequest.clientRegistration.registrationId)) {
                throw OAuth2AuthenticationProcessingException("Looks like you're signed up with ${user.authProvider} account. " +
                        "Please use your ${user.authProvider} account to login.")
            }
            user = updateExistingUser(user, oAuth2UserInfo)
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
        }

        return UserPrincipal.of(user, oAuth2User.attributes)
    }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): User {
        val user = User(
            nickName = oAuth2UserInfo.getName(),
            email = oAuth2UserInfo.getEmail(),
            password = oAuth2UserInfo.getId(),
            role = RoleType.ROLE_USER,
            authProvider = AuthProvider.of(oAuth2UserRequest.clientRegistration.registrationId),
            providerId = oAuth2UserInfo.getId()
        )
        return userRepository.save(user)
    }

    private fun updateExistingUser(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User {
        existingUser.email = oAuth2UserInfo.getEmail()
        return existingUser
    }
}
