package com.moa.bookmark.controller

import com.moa.auth.domain.UserPrincipal
import com.moa.bookmark.controller.request.BookmarkCreateRequest
import com.moa.bookmark.controller.response.BookmarkCreateResponse
import com.moa.bookmark.domain.BookmarkService
import com.moa.common.ApiResponse
import com.moa.user.domain.LoginUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/bookmarks")
class BookmarkController(
    private val bookmarkService: BookmarkService
) {
    @PostMapping
    fun create(
        @LoginUser user: UserPrincipal,
        @RequestBody request: BookmarkCreateRequest
    ): ResponseEntity<ApiResponse> {
        val bookmark = bookmarkService.create(user.getId(), request)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/v1/bookmarks/${bookmark.bookmarkId}")
            .buildAndExpand(bookmark).toUri()

        return ResponseEntity
            .created(location)
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = BookmarkCreateResponse(bookmark.bookmarkId)
                )
            )
    }

    @GetMapping
    fun findList(@LoginUser user: UserPrincipal): ResponseEntity<ApiResponse> {
        val bookmarks = bookmarkService.findList(user.getId())

        return ResponseEntity
            .ok(ApiResponse(data = bookmarks))
    }

    @GetMapping("/{id}")
    fun findDetail(
        @LoginUser user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse> {
        val bookmark = bookmarkService.findDetail(id)

        return ResponseEntity
            .ok(ApiResponse(data = bookmark))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        bookmarkService.delete(id)

        return ResponseEntity
            .noContent()
            .build()
    }
}
