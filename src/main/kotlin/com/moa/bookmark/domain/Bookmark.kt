package com.moa.bookmark.domain

import com.moa.common.BaseEntity
import com.moa.recommendation.domain.Recommendation
import javax.persistence.*

@Entity
class Bookmark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    var recommendation: Recommendation
) : BaseEntity()
