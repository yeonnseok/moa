package com.moa.bookmark.domain

import com.moa.bookmark.controller.request.BookmarkCreateRequest
import com.moa.recommendation.domain.*
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
    private lateinit var contentRepository: ContentRepository

    @Autowired
    private lateinit var recommendationRepository: RecommendationRepository

    @Test
    fun `북마크 생성`() {
        // given
        val content = contentRepository.save(
            Content(
                title = "Spider Man",
                contents = "peter parker",
                minValue = 10,
                maxValue = 20,
                type = ContentType.MOVIE
            )
        )
        val recommendation = recommendationRepository.save(
            Recommendation(
                userId = 1,
                recordId = 1,
                content = content
            )
        )
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

    }

    @Test
    fun `북마크 상세 조회`() {

    }

    @Test
    fun `북마크 삭제`() {

    }
}