package com.moa.admin

import com.moa.admin.dto.ContentRequest
import com.moa.admin.dto.DescriptionRequest
import com.moa.common.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/v1")
class AdminController(
    private val descriptionService: DescriptionService,
    private val contentService: ContentService
) {
    @PostMapping("/descriptions")
    fun createDescription(@RequestBody request: DescriptionRequest): ResponseEntity<ApiResponse> {
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

    @PostMapping("/contents")
    fun createContent(@RequestBody request: ContentRequest): ResponseEntity<ApiResponse> {
        val description = contentService.create(request)
        return ResponseEntity.ok(ApiResponse(data = description))
    }

    @PatchMapping("/contents/{id}")
    fun updateContent(
        @PathVariable id: Long,
        @RequestBody request: ContentRequest
    ): ResponseEntity<ApiResponse> {
        contentService.update(id, request)
        return ResponseEntity.ok(ApiResponse(data = null))
    }

    @DeleteMapping("/contents/{id}")
    fun deleteContent(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse> {
        contentService.delete(id)
        return ResponseEntity.ok(ApiResponse(data = null))
    }
}