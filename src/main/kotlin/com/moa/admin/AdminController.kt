package com.moa.admin

import com.moa.admin.dto.DescriptionRequest
import com.moa.common.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/v1")
class AdminController(
    private val descriptionService: DescriptionService
) {
    @PostMapping("/descriptions")
    fun createDescription(request: DescriptionRequest): ResponseEntity<ApiResponse> {
        val description = descriptionService.create(request)
        return ResponseEntity.ok(ApiResponse(data = description))
    }

    @PatchMapping("/descriptions/{id}")
    fun updateDescription(
        @PathVariable id: Long,
        @RequestBody request: DescriptionRequest
    ): ResponseEntity<ApiResponse> {
        descriptionService.update(id, request)
        return ResponseEntity.ok(ApiResponse(data = null))
    }

    @DeleteMapping("/descriptions/{id}")
    fun deleteDescription(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse> {
        descriptionService.delete(id)
        return ResponseEntity.ok(ApiResponse(data = null))
    }
}