package com.moa.auth.domain

import com.moa.exceptions.UserNotFoundException
import com.moa.user.domain.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            return UserPrincipal(
                email = user.email,
                password = user.password,
                authorities = listOf(SimpleGrantedAuthority(user.role.name))
            )
        }
        throw UserNotFoundException()
    }
}
