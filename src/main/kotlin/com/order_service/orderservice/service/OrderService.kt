package com.order_service.orderservice.service

import com.order_service.orderservice.domain.dto.request.ReqOrderDto
import com.order_service.orderservice.domain.dto.response.ResOrderDto

interface OrderService {
    fun createOrder(request: ReqOrderDto): ResOrderDto
    fun getOrderByUser(): List<ResOrderDto>
    fun updateOrder(orderId: Int)
}