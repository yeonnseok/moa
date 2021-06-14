package com.moa.recommendation.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ContentRepository : JpaRepository<Content, Long> {

    fun findByMinValueLessThanEqualAndMaxValueGreaterThanEqual(score: Int, scoreCopy: Int): List<Content>

    fun findAllByOrderByIdAsc(): List<Content>
}
