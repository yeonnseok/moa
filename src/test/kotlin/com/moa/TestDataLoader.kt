package com.moa

import com.moa.bookmark.domain.Bookmark
import com.moa.bookmark.domain.BookmarkRepository
import com.moa.recommendation.domain.*
import com.moa.record.domain.*
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@Component
@ActiveProfiles("test")
class TestDataLoader(
    private val userRepository: UserRepository,
    private val recordRepository: RecordRepository,
    private val emotionRepository: EmotionRepository,
    private val descriptionRepository: DescriptionRepository,
    private val recommendationRepository: RecommendationRepository,
    private val contentRepository: ContentRepository,
    private val bookmarkRepository: BookmarkRepository,

    private val passwordEncoder: PasswordEncoder
) {
    fun sample_user() = userRepository.save(
        User(
            nickName = "moa",
            email = "moa@com",
            password = passwordEncoder.encode("m123"),
            profileEmotionType = EmotionType.HAPPY,
            role = RoleType.ROLE_USER
        )
    )

    fun sample_description_14_to_16() = descriptionRepository.save(
        Description(
            minValue = 14,
            maxValue = 16,
            description = "산뜻하고 행복한 기분"
        )
    )

    fun sample_description_36_to_40() = descriptionRepository.save(
        Description(
            minValue = 36,
            maxValue = 40,
            description = "롤러코스터같이 널뛰기하는 기분"
        )
    )

    fun sample_content_spiderman() = contentRepository.save(
        Content(
            title = "Spider Man",
            contents = "peter parker",
            minValue = 10,
            maxValue = 20,
            type = ContentType.MOVIE
        )
    )

    fun sample_content_soul() = contentRepository.save(
        Content(
            title = "소울",
            contents = """
                    음악을 가르치는 조는 학생들에게 음악이 무엇인지 재즈가 무엇인지를 가르칩니다. 조는 재즈 연주가가 되는 것이 인생의 최대 목표입니다. 하지만 원하던 기회가 찾아왔을 때, 맨홀에 빠지게 되며 죽음의 세계로 들어가게 됩니다.

                    이 영화는 모든 사람에게 삶의 목적이 필요한 것인지 다시 되돌아보게 합니다. 단순하지만 묵직한 울림을 계속 던져주는 영화입니다.

                    애니메이션을 넘어서 많은 생각과, 고민 또 그간의 쌓여온 감정들을 자세히 되돌아보게 되는 질문이 가득한 영화였습니다.
                """.trimIndent(),
            minValue = 10,
            maxValue = 20,
            type = ContentType.MOVIE
        )
    )

    fun sample_recommendation_by(userId: Long, content: Content) = recommendationRepository.save(
        Recommendation(
            userId = userId,
            recordId = 1,
            content = content
        )
    )

    fun sample_recommendation_by(userId: Long, recordId: Long, content: Content) = recommendationRepository.save(
        Recommendation(
            userId = userId,
            recordId = recordId,
            content = content
        )
    )

    fun sample_record_by(userId: Long) = recordRepository.save(
        Record(
            userId = userId,
            recordDate = LocalDate.of(2021, 5, 5),
            keywords = setOf(Keyword.STUDY, Keyword.MONEY),
            memo = "first memo"
        )
    )

    fun sample_record_first_by(userId: Long) = recordRepository.save(
        Record(
            userId = userId,
            recordDate = LocalDate.of(2021, 5, 6),
            keywords = setOf(Keyword.FAMILY, Keyword.FRIEND),
            memo = "first record"
        )
    )

    fun sample_record_second_by(userId: Long) = recordRepository.save(
        Record(
            userId = userId,
            recordDate = LocalDate.of(2021, 5, 7),
            keywords = setOf(Keyword.FAMILY, Keyword.FRIEND),
            memo = "second record"
        )
    )

    fun sample_bookmark_by(recommendation: Recommendation) = bookmarkRepository.save(
        Bookmark(
            userId = recommendation.userId,
            recommendation = recommendation
        )
    )

    fun sample_emotion_happy_by(record: Record, count: Int) = emotionRepository.save(
        Emotion(
            record = record,
            emotionType = EmotionType.HAPPY,
            count = count
        )
    )

    fun sample_emotion_happy_and_excited_by(record1: Record, record2: Record) = emotionRepository.saveAll(listOf(
        Emotion(
            record = record1,
            emotionType = EmotionType.HAPPY,
            count = 10
        ),
        Emotion(
            record = record2,
            emotionType = EmotionType.EXCITED,
            count = 5
        )
    ))
}