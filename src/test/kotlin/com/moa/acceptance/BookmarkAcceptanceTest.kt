package com.moa.acceptance

import com.moa.bookmark.controller.request.BookmarkCreateRequest
import com.moa.bookmark.controller.response.BookmarkCreateResponse
import com.moa.bookmark.controller.response.BookmarkResponse
import com.moa.common.ResultType
import com.moa.recommendation.domain.*
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import java.util.stream.Stream

class BookmarkAcceptanceTest : AcceptanceTest() {

    @Autowired
    private lateinit var contentRepository: ContentRepository

    @Autowired
    private lateinit var recommendationRepository: RecommendationRepository

    private var recommendation: Recommendation? = null

    private var bookmarkId: Long? = null

    @DisplayName("북마크 인수테스트")
    @TestFactory
    fun manageRecommendation(): Stream<DynamicTest> {
        return Stream.of(
            DynamicTest.dynamicTest("추천 받은 컨텐츠 북마크하기", {
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
                recommendation = recommendationRepository.save(
                    Recommendation(
                        userId = userId!!,
                        recordId = 1,
                        content = content
                    )
                )
                val request = BookmarkCreateRequest(
                    recommendationId = 1
                )

                // when
                val response = post("/api/v1/bookmarks", request)

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.CREATED.value()
                val createResponse = getResponseData(response.data, BookmarkCreateResponse::class.java) as BookmarkCreateResponse
                createResponse.bookmarkId shouldBe 1
            }),

            DynamicTest.dynamicTest("북마크 목록 조회하기", {
                // when
                val response = get("/api/v1/bookmarks")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()
                response.data shouldNotBe null
                // TODO
            }),

            DynamicTest.dynamicTest("북마크 상세 조회하기", {
                // when
                val response = get("/api/v1/bookmarks/${bookmarkId}")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()
                val data = (response.data as BookmarkResponse)
                data.record.recordId shouldBe recommendation!!.recordId
                data.recommendation.title shouldBe recommendation!!.content.title
                data.recommendation.contents shouldBe recommendation!!.content.contents
                data.recommendation.type shouldBe recommendation!!.content.type
            }),

            DynamicTest.dynamicTest("북마크 취소하기", {
                // when
                delete("/api/v1/bookmarks/$bookmarkId")
            })
        )
    }
}
