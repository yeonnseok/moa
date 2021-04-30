package com.moa.record.domain

import org.springframework.data.jpa.repository.JpaRepository

interface DescriptionRepository : JpaRepository<Description, Long> {

    fun findByMaxValueGreaterThanEqualAndMinValueLessThanEqual(score: Int, scoreCopy: Int): List<Description>
}
