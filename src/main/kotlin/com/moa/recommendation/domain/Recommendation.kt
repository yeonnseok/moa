package com.moa.recommendation.domain

import com.moa.common.BaseEntity
import javax.persistence.*

@Entity
data class Recommendation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "record_id", nullable = false)
    var recordId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    var content: Content,

    @Column(name = "description", nullable = false)
    var description: String
) : BaseEntity()
