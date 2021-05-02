package com.moa.acceptance

import com.moa.common.ResultType
import io.kotlintest.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.http.HttpStatus
import java.util.stream.Stream

class RecommendationAcceptanceTest : AcceptanceTest() {

    @DisplayName("추천 컨텐츠 인수테스트")
    @TestFactory
    fun manageRecommendation(): Stream<DynamicTest> {
        return Stream.of(
            DynamicTest.dynamicTest("감정 온도 기반으로 컨텐츠 추천 받기", {
                // given
                dataLoader.sample_content_spiderman()

                // when
                val response = get("/api/v1/recommendations?recordId=1&score=16")

                // then
                response.result shouldBe ResultType.SUCCESS
                response.statusCode shouldBe HttpStatus.OK.value()
            })
        )
    }
}
