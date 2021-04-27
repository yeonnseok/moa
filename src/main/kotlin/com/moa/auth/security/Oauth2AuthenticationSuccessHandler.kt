package com.moa.auth.security

import com.moa.common.utils.CookieUtils
import com.moa.exceptions.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class Oauth2AuthenticationSuccessHandler(
    private val tokenProvider: JwtTokenProvider,
    private val cookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    @Value("\${security.authorized-redirect-uri:}")
    private val authorizedRedirectUri: String
) : SimpleUrlAuthenticationSuccessHandler() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            log.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl)

    }

    fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        cookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri = CookieUtils.getCookie(
                request = request,
                name = cookieOAuth2AuthorizationRequestRepository.redirectUriParamCookieName
            )?.value

        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw BadRequestException()
        }

        val targetUrl = redirectUri ?: defaultTargetUrl

        val token = tokenProvider.createToken(authentication)

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", token)
            .build().toUriString()
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)
        val authorizedURI: URI = URI.create(authorizedRedirectUri)
        if (authorizedURI.getHost().equals(clientRedirectUri.getHost(), ignoreCase = true)
            && authorizedURI.getPort() == clientRedirectUri.getPort()
        ) {
            return true
        }
        return false
    }
}
