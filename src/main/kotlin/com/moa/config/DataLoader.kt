package com.moa.config

import com.moa.record.domain.Description
import com.moa.record.domain.DescriptionRepository
import com.moa.record.domain.EmotionType
import com.moa.user.domain.AuthProvider
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DataLoader(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val descriptionRepository: DescriptionRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {

        // Save Descriptions
        val descriptions = listOf(
            Description(
                minValue = -1,
                maxValue = 1,
                description = "설명할 수 없는 기분"
            ),
            Description(
                minValue = -1,
                maxValue = 1,
                description = "그때그때 다른 내 기분"
            ),
            Description(
                minValue = 2,
                maxValue = 4,
                description = "티끌모아 태산같은 기분"
            ),
            Description(
                minValue = 5,
                maxValue = 7,
                description = "물음표 던지다 느낌표가 찾아온 기분"
            ),
            Description(
                minValue = 8,
                maxValue = 10,
                description = "우연한 즐거움을 마주한 기분"
            ),
            Description(
                minValue = 11,
                maxValue = 13,
                description = "몽글몽글한 기분"
            ),
            Description(
                minValue = 14,
                maxValue = 16,
                description = "산뜻하고 행복한 기분"
            ),
            Description(
                minValue = 17,
                maxValue = 20,
                description = "유쾌하고 상큼한 기분"
            ),
            Description(
                minValue = 21,
                maxValue = 25,
                description = "여유롭고 쾌적한 기분"
            ),
            Description(
                minValue = 26,
                maxValue = 30,
                description = "눈밭에 처음 발자국 찍는 기분"
            ),
            Description(
                minValue = 31,
                maxValue = 35,
                description = "마라톤을 완주한 기분"
            ),
            Description(
                minValue = 36,
                maxValue = 40,
                description = "롤러코스터같이 널뛰기하는 기분"
            )
        )
        descriptionRepository.saveAll(descriptions)
    }
}
