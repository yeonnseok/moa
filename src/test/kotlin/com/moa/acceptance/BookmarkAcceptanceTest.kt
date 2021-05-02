package com.moa.acceptance

import com.moa.bookmark.controller.request.BookmarkCreateRequest
import com.moa.bookmark.controller.response.BookmarkCreateResponse
import com.moa.bookmark.controller.response.BookmarkResponse
import com.moa.common.ResultType
import com.moa.recommendation.domain.Recommendation
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.http.HttpStatus
import java.util.stream.Stream

class BookmarkAcceptanceTest : AcceptanceTest() {

    private var recommendation: Recommendation? = null

    private var bookmarkId: Long? = null

    @DisplayName("북마크 인수테스트")
    @TestFactory
    fun manageBookmark(): Stream<DynamicTest> {
        return Stream.of(
            DynamicTest.dynamicTest("추천 받은 컨텐츠 북마크하기", {
                // given
                val record = dataLoader.sample_record_by(userId!!)
                val content = dataLoader.sample_content_spiderman()
                recommendation = dataLoader.sample_recommendation_by(userId!!, record.id!!, content)

                val request = BookmarkCreateRequest(
                    recommendationId = recommendation!!.id!!
                )

                // when
                val response = post("/api/v1/bookmarks", request)

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.CREATED.value()

                val createResponse = getResponseData(response.data, BookmarkCreateResponse::class.java) as BookmarkCreateResponse
                createResponse.bookmarkId shouldNotBe null

                bookmarkId = createResponse.bookmarkId
            }),

            DynamicTest.dynamicTest("북마크 목록 조회하기", {
                // when
                val response = get("/api/v1/bookmarks")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()
            }),

            DynamicTest.dynamicTest("북마크 상세 조회하기", {
                // when
                val response = get("/api/v1/bookmarks/${bookmarkId}")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()

                val detail = getResponseData(response.data, BookmarkResponse::class.java) as BookmarkResponse
                detail.recordId shouldBe recommendation!!.recordId
                detail.title shouldBe recommendation!!.content.title
                detail.contents shouldBe recommendation!!.content.contents
                detail.type shouldBe recommendation!!.content.type
            }),

            DynamicTest.dynamicTest("북마크 취소하기", {
                // when
                delete("/api/v1/bookmarks/$bookmarkId")
            })
        )
    }
}
