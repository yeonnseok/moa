package com.moa.record.controller

import com.moa.auth.domain.UserPrincipal
import com.moa.common.ApiResponse
import com.moa.common.utils.yyyy_MM_dd_Formatter
import com.moa.record.controller.request.RecordCreateRequest
import com.moa.record.controller.request.RecordUpdateRequest
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

    @GetMapping(params = ["recordDate"])
    fun findDaily(
        @LoginUser user: UserPrincipal,
        @RequestParam recordDate: String
    ): ResponseEntity<ApiResponse> {
        val recordResponse = recordService.findDaily(user.getId(), recordDate.yyyy_MM_dd_Formatter())

        return ResponseEntity
            .ok(ApiResponse(data = recordResponse))
    }

    @GetMapping(params = ["fromDate", "toDate"])
    fun findWeekly(
        @LoginUser user: UserPrincipal,
        @RequestParam fromDate: String,
        @RequestParam toDate: String
    ): ResponseEntity<ApiResponse> {
        val weeklyRecordResponse = recordService.findWeekly(
            userId = user.getId(),
            fromDate = fromDate.yyyy_MM_dd_Formatter(),
            toDate = toDate.yyyy_MM_dd_Formatter()
        )

        return ResponseEntity
            .ok(ApiResponse(data = weeklyRecordResponse))
    }

    @GetMapping(path = ["/month"],params = ["fromDate", "toDate"])
    fun findMonthly(
        @LoginUser user: UserPrincipal,
        @RequestParam fromDate: String,
        @RequestParam toDate: String
    ): ResponseEntity<ApiResponse> {
        val responses = recordService.findMonthly(
            userId = user.getId(),
            fromDate = fromDate.yyyy_MM_dd_Formatter(),
            toDate = toDate.yyyy_MM_dd_Formatter()
        )

        return ResponseEntity
            .ok(ApiResponse(data = responses))
    }

    @PatchMapping("/{id}")
    fun update(
            @LoginUser user: UserPrincipal,
            @PathVariable id: Long,
            @RequestBody request: RecordUpdateRequest
    ): ResponseEntity<ApiResponse> {
        val recordResponse = recordService.update(user.getId(), id, request)
        return ResponseEntity
                .ok(ApiResponse(data = recordResponse))
    }

    @DeleteMapping("/{id}")
    fun delete(
            @LoginUser user: UserPrincipal,
            @PathVariable id: Long
    ): ResponseEntity<Unit> {
        recordService.delete(user.getId(), id)
        return ResponseEntity
                .noContent()
                .build()
    }

}
