package com.moa.config

import com.moa.record.domain.Description
import com.moa.record.domain.DescriptionRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DataLoader(
    private val descriptionRepository: DescriptionRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {

        // Save Descriptions
        val descriptions = listOf(
            Description(
                minValue = 36,
                maxValue = 40,
                description = "롤러코스터같이 널뛰기하는 기분"
            )
        )
        descriptionRepository.saveAll(descriptions)
    }
}
