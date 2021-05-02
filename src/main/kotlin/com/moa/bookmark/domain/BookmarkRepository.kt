package com.moa.bookmark.domain

import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, Long> {

    fun findByUserId(userId: Long): List<Bookmark>
}
