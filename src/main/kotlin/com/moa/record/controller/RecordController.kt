package com.moa.record.controller

import com.moa.auth.domain.UserPrincipal
import com.moa.common.ApiResponse
import com.moa.common.utils.yyyy_MM_dd_Formatter
import com.moa.record.controller.request.RecordCreateRequest
import com.moa.record.controller.response.RequestCreateResponse
import com.moa.record.domain.RecordService
import com.moa.user.domain.LoginUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/records")
class RecordController(
    private val recordService: RecordService
) {
    @PostMapping
    fun create(
        @LoginUser user: UserPrincipal,
        @RequestBody request: RecordCreateRequest
    ): ResponseEntity<ApiResponse> {
        val recordId = recordService.create(user.getId(), request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/v1/records/$recordId")
            .buildAndExpand(recordId).toUri()

        return ResponseEntity
            .created(location)
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = RequestCreateResponse(recordId)
                )
            )
    }

    @GetMapping
    fun find(
        @LoginUser user: UserPrincipal,
        @RequestParam recordDate: String
    ): ResponseEntity<ApiResponse> {
        val recordResponse = recordService.find(user.getId(), recordDate.yyyy_MM_dd_Formatter())

        return ResponseEntity
            .ok(ApiResponse(data = recordResponse))
    }
}
