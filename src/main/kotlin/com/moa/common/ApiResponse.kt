package com.moa.common

class ApiResponse(
    val result: ResultType = ResultType.SUCCESS,
    val statusCode: Int = 200,
    val data: Any
)
