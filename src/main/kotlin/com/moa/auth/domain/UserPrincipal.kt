package com.moa.auth.domain

import com.moa.user.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    private val id: Long,
    private val email: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>,
    private val attributes: Map<String, Any>? = null
) : UserDetails, OAuth2User {
    companion object {
        fun of(user: User): UserPrincipal {
            return UserPrincipal(
                id = user.id!!,
                email = user.email,
                password = user.password,
                authorities = listOf(SimpleGrantedAuthority(user.role.name))
            )
        }

        fun of(user: User, attributes: Map<String, Any>): UserPrincipal {
            return UserPrincipal(
                id = user.id!!,
                email = user.email,
                password = user.password,
                authorities = listOf(SimpleGrantedAuthority(user.role.name)),
                attributes = attributes
            )
        }
    }

    fun getId() = this.id

    override fun getName() = this.id.toString()

    override fun getAttributes() = this.attributes

    override fun getAuthorities() = this.authorities

    override fun getPassword() = this.password

    override fun getUsername() = this.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
