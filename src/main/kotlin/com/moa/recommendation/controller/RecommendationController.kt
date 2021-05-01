package com.moa.recommendation.controller

import com.moa.auth.domain.UserPrincipal
import com.moa.common.ApiResponse
import com.moa.recommendation.domain.RecommendationService
import com.moa.user.domain.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/recommendations")
class RecommendationController(
    private val recommendationService: RecommendationService
) {
    @GetMapping(params = ["recordId", "score"])
    fun recommend(
        @LoginUser user: UserPrincipal,
        @RequestParam recordId: Long,
        @RequestParam score: Int
    ): ResponseEntity<ApiResponse> {
        val recommendation = recommendationService.recommend(
            userId = user.getId(),
            recordId = recordId,
            score = score
        )

        return ResponseEntity
            .ok(ApiResponse(data = recommendation))
    }
}
