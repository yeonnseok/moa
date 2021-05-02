package com.moa.bookmark.domain

import com.moa.TestDataLoader
import com.moa.bookmark.controller.request.BookmarkCreateRequest
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class BookmarkServiceTest {

    @Autowired
    private lateinit var sut: BookmarkService

    @Autowired
    private lateinit var dataLoader: TestDataLoader

    @Test
    fun `북마크 생성`() {
        // given
        val content = dataLoader.sample_content_spiderman()
        val recommendation = dataLoader.sample_recommendation_by(1, content)

        val request = BookmarkCreateRequest(
            recommendationId = recommendation.id!!
        )

        // when
        val bookmark = sut.create(1, request)

        // then
        bookmark.bookmarkId shouldNotBe null
    }

    @Test
    fun `북마크 목록 조회`() {
        // given
        val record = dataLoader.sample_record_by(1)
        val content = dataLoader.sample_content_spiderman()
        val recommendation = dataLoader.sample_recommendation_by(record.userId, record.id!!, content)
        dataLoader.sample_bookmark_by(recommendation)

        // when
        val bookmarks = sut.findList(recommendation.userId)

        // then
        bookmarks.size shouldBe 1
        bookmarks[0].recordId shouldBe record.id!!
        bookmarks[0].title shouldBe content.title
        bookmarks[0].contents shouldBe content.contents
        bookmarks[0].type shouldBe content.type
    }

    @Test
    fun `북마크 상세 조회`() {

    }

    @Test
    fun `북마크 삭제`() {

    }
}