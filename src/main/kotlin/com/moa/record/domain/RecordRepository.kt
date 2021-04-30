package com.moa.record.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface RecordRepository : JpaRepository<Record, Long> {

    fun findByUserIdAndRecordDate(userId: Long, recordDate: LocalDate): Record?

    fun existsByUserIdAndRecordDate(userId: Long, recordDate: LocalDate): Boolean
}
