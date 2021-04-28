package com.moa.config

import com.moa.auth.domain.CustomOAuth2UserService
import com.moa.auth.domain.CustomUserDetailsService
import com.moa.auth.filter.TokenAuthenticationFilter
import com.moa.auth.security.*
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenAuthenticationFilter: TokenAuthenticationFilter,
    private val customUserDetailsService: CustomUserDetailsService,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oauth2AuthenticationSuccessHandler: Oauth2AuthenticationSuccessHandler,
    private val oauth2AuthenticationFailureHandler: Oauth2AuthenticationFailureHandler,
    private val cookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : WebSecurityConfigurerAdapter() {

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Throws(Exception::class)
    override fun authenticationManagerBean() = super.authenticationManagerBean()!!

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger/**"
        ).requestMatchers(PathRequest.toStaticResources().atCommonLocations())
    }

    override fun configure(http: HttpSecurity) {
        http
            .cors().and()
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .logout()
            .logoutSuccessUrl("/")

        http
            .exceptionHandling()
            .authenticationEntryPoint(RestAuthenticationEntryPoint())

        http
            .authorizeRequests()
            .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .antMatchers("/api/v1/auth/**", "/login/oauth2/**").permitAll()
            .antMatchers("/").permitAll()
            .anyRequest().authenticated()

        http.oauth2Login()
            .authorizationEndpoint()
            .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository)
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(oauth2AuthenticationSuccessHandler)
            .failureHandler(oauth2AuthenticationFailureHandler)

        http
            .addFilterBefore(
                tokenAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
