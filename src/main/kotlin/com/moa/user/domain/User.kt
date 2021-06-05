package com.moa.user.domain

import com.moa.common.BaseEntity
import com.moa.record.domain.EmotionType
import org.hibernate.annotations.Type
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nick_name", nullable = true)
    var nickName: String? = null,

    @Email
    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: RoleType,

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_emotion")
    var profileEmotionType: EmotionType? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    var authProvider: AuthProvider,

    @Type(type="yes_no")
    @Column(name = "onboarding_flag", nullable = false)
    var onboardingFlag: Boolean = false
) : BaseEntity()
