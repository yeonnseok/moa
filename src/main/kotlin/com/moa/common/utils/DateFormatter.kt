package com.moa.common.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.yyyy_MM_dd_Formatter() =
    LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
