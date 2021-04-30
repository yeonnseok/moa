package com.moa.record.domain

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [
    UniqueConstraint(columnNames = ["min_value", "max_value", "description"])
])
data class Description(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "min_value", nullable = false)
    var minValue: Int,

    @Column(name = "max_value", nullable = false)
    var maxValue: Int,

    @Column(name = "description", nullable = false)
    var description: String
)
