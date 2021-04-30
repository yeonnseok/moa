package com.moa.record.domain

import com.moa.common.BaseEntity
import java.time.LocalDate
import javax.persistence.*

@Entity
class Record(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "record_date", nullable = false)
    var recordDate: LocalDate,

    @ElementCollection(targetClass = Keyword::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "record_keywords")
    @Enumerated(EnumType.STRING)
    @Column(name = "keyword")
    var keywords: Set<Keyword>?,

    @Column(name = "memo", nullable = true)
    var memo: String?
) : BaseEntity() {

    private val maxScore = 40
    private val minScore = -40

    @OneToMany(mappedBy = "record", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var emotions: List<Emotion> = ArrayList()

    fun addEmotions(emotionMap: Map<String, Int>) {
        this.emotions = emotionMap.map {
            Emotion(
                record = this,
                emotionType = EmotionType.of(it.key),
                count = it.value
            )
        }
    }

    fun totalScore(): Int {
        val score = emotions.map {
            it.emotionType.score * it.count
        }.sum()

        when {
            score >= maxScore -> {
                return maxScore
            }
            score <= minScore -> {
                return minScore
            }
            else -> return score
        }
    }
}
