package com.order_service.orderservice.exception

class CustomException (
    val exceptionMessage: String,
    val statusCode: Int,
    val data: Any? = null
):RuntimeException()