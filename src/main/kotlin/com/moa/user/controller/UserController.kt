package com.moa.user.controller

import com.moa.auth.domain.UserPrincipal
import com.moa.common.ApiResponse
import com.moa.user.controller.request.UserUpdateRequest
import com.moa.user.controller.response.UserResponse
import com.moa.user.domain.LoginUser
import com.moa.user.domain.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun find(@LoginUser user: UserPrincipal): ResponseEntity<ApiResponse> {
        val findUser = userService.find(user.getId())
        return ResponseEntity
            .ok(ApiResponse(data = UserResponse.of(findUser)))
    }

    @PatchMapping("/me")
    fun update(
        @LoginUser user: UserPrincipal,
        @RequestBody request: UserUpdateRequest
    ): ResponseEntity<ApiResponse> {
        val updatedUser = userService.update(user.getId(), request)
        return ResponseEntity
            .ok(ApiResponse(data = UserResponse.of(updatedUser)))
    }
}
