package com.moa.user.domain

import com.moa.common.BaseEntity
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nick_name", nullable = false)
    var nickName: String,

    @Email
    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: RoleType,

    @Column(name = "image")
    var image: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    var authProvider: AuthProvider = AuthProvider.LOCAL,

    @Column(name = "provider_id")
    var providerId: String? = null
) : BaseEntity()
