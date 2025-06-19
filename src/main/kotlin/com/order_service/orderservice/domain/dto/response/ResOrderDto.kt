package com.order_service.orderservice.domain.dto.response

import java.math.BigDecimal

class ResOrderDto (
    val orderId: Long,
    val totalAmount: BigDecimal,
    val orderNumber: String,
    val status: String,
    val items: List<OrderItemDetail>
)

data class OrderItemDetail(
    val productId: Int,
    val producName: String,
    val quantity: Int,
    val price: BigDecimal
)