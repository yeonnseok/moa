package com.moa.record.controller

import com.moa.record.domain.DescriptionRepository
import com.moa.restdocs.LoginUserControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

class DescriptionControllerTest : LoginUserControllerTest() {

    @Autowired
    private lateinit var descriptionRepository: DescriptionRepository

    @Test
    fun `대표 감정 API`() {

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/descriptions?score=40")
                .header("Authorization", "Bearer $token")
        )
    }
}