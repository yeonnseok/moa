package com.moa.recommendation.controller

import com.moa.common.ApiResponse
import com.moa.exceptions.RecommendationNotFoundException
import com.moa.recommendation.domain.ContentFinder
import com.moa.recommendation.domain.RecommendationRepository
import com.moa.recommendation.domain.RecommendationService
import com.moa.recommendation.domain.dto.ContentResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/contents")
class ContentController(
    private val recommendationService: RecommendationService
) {
    @GetMapping
    fun find(@RequestParam recommendationId: Long): ResponseEntity<ApiResponse> {
        val recommendation = recommendationService.find(recommendationId)

        return ResponseEntity.ok(
            ApiResponse(
                data = ContentResponse.of(recommendation.content, recommendation.description)
            )
        )
    }
}
