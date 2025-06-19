package com.order_service.orderservice.domain.dto.response

import java.math.BigDecimal

data class ResAllProductDto(
    val id: Int,
    val name: String,
    val description: String? = null,
    val price: BigDecimal = BigDecimal.ZERO,
    val stock: Int = 0,
    val categoryId: Int,
    val categoryName: String,
    val createdBy : String? = null
)
