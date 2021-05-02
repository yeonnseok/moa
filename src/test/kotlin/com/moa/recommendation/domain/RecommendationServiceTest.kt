package com.moa.recommendation.domain

import com.moa.TestDataLoader
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class RecommendationServiceTest {

    @Autowired
    private lateinit var sut: RecommendationService

    @Autowired
    private lateinit var dataLoader: TestDataLoader

    @Test
    fun `점수 기반 추천 컨텐츠 조회`() {
        // given
        val content = dataLoader.sample_content_spiderman()

        // when
        val recommendation = sut.recommend(1, 1, 16)

        // then
        recommendation.title shouldBe content.title
        recommendation.contents shouldBe content.contents
        recommendation.type shouldBe content.type
    }
}
