package com.moa.auth.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    private val email: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
): UserDetails {
    override fun getAuthorities() = this.authorities

    override fun getPassword() = this.password

    override fun getUsername() = this.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}