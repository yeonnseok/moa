package com.moa.record.controller.request

import java.time.LocalDate

data class RecordCreateRequest(
    val recordDate: LocalDate = LocalDate.now(),
    val emotions: Map<String, Int>,
    val keywords: Set<String>?,
    val memo: String?
)
