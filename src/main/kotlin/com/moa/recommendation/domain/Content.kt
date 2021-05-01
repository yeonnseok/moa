package com.moa.recommendation.domain

import javax.persistence.*

@Entity
data class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Lob
    @Column(name = "contents", nullable = false)
    var contents: String,

    @Column(name = "min_value", nullable = false)
    var minValue: Int,

    @Column(name = "max_value", nullable = false)
    var maxValue: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    var type: ContentType
)
