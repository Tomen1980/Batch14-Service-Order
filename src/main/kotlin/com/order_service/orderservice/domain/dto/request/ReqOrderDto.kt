package com.order_service.orderservice.domain.dto.request

data class ReqOrderDto(
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val productId: Int,
    val quantity: Int,
    val createdBy: Int,

)
