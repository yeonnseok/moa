package com.moa.config

import com.moa.record.domain.DescriptionRepository
import com.moa.record.domain.EmotionType
import com.moa.user.domain.AuthProvider
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminLoader(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {

        // Save Admin
        userRepository.save(
            User(
                nickName = "admin",
                email = "admin@com",
                password = passwordEncoder.encode("moaadmin"),
                role = RoleType.ROLE_ADMIN,
                profileEmotionType = EmotionType.HAPPY,
                authProvider = AuthProvider.LOCAL,
                onboardingFlag = true
            )
        )
    }
}
