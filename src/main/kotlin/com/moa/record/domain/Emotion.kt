package com.moa.record.domain

import com.moa.common.BaseEntity
import javax.persistence.*

@Entity
class Emotion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    var record: Record,

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion_type", nullable = false)
    var emotionType: EmotionType,

    @Column(name = "count", nullable = false)
    var count: Int
) : BaseEntity()
