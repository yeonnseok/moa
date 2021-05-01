package com.moa.record.controller

import com.moa.common.ApiResponse
import com.moa.record.controller.response.DescriptionResponse
import com.moa.record.domain.DescriptionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/descriptions")
class DescriptionController(
    private val descriptionService: DescriptionService
) {
    @GetMapping
    fun find(@RequestParam score: Int): ResponseEntity<ApiResponse> {
        val description = descriptionService.find(score)

        return ResponseEntity
            .ok(ApiResponse(data = DescriptionResponse(description.description)))
    }
}
