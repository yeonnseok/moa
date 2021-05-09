package com.moa.record.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface RecordRepository : JpaRepository<Record, Long> {

    fun existsByUserIdAndRecordDate(userId: Long, recordDate: LocalDate): Boolean

    fun findByUserIdAndRecordDate(userId: Long, recordDate: LocalDate): Record?

    fun findByUserIdAndRecordDateGreaterThanEqualAndRecordDateLessThanEqual(userId: Long, fromDate: LocalDate, toDate: LocalDate): List<Record>

    fun findByRecordDateGreaterThanEqualAndRecordDateLessThanEqual(fromDate: LocalDate, toDate: LocalDate): List<Record>
}
