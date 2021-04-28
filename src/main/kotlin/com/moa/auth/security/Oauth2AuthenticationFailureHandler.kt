package com.moa.auth.security

import com.moa.common.utils.CookieUtils
import net.minidev.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class Oauth2AuthenticationFailureHandler(
    private val cookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationFailureHandler() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        var targetUrl = CookieUtils.getCookie(
                request = request,
                name = cookieOAuth2AuthorizationRequestRepository.redirectUriParamCookieName
            )?.value ?: "/"

        log.error("exception message {}", exception.message)

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .build().toUriString()

        cookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}