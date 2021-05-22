package com.moa.common

import org.springframework.http.HttpStatus

class ApiResponse(
    val result: ResultType = ResultType.SUCCESS,
    val statusCode: Int = HttpStatus.OK.value(),
    val data: Any?
)
