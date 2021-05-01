package com.moa.auth.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class EmailCheckRequest(

    @Email
    @NotBlank
    val email: String
)
