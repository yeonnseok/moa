package com.moa.user.controller

import com.moa.auth.controller.resolver.LoginUser
import com.moa.auth.controller.response.UserResponse
import com.moa.common.ApiResponse
import com.moa.user.controller.request.UserUpdateRequest
import com.moa.user.domain.User
import com.moa.user.domain.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/{id}")
    fun find(@LoginUser user: User): ResponseEntity<ApiResponse> {
        return ResponseEntity
            .ok(ApiResponse(data = UserResponse.of(user)))
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UserUpdateRequest
    ): ResponseEntity<ApiResponse> {
        userService.update(id, request)

        return ResponseEntity
            .noContent()
            .build()
    }
}
