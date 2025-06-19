package com.order_service.orderservice.rest

import com.order_service.orderservice.domain.dto.response.BaseResponse
import com.order_service.orderservice.domain.dto.response.ResAllProductDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name="product-service",
    path = "/product-service"
)
interface ProductManagementClient {
    @GetMapping("v1/products/{id}")
    fun getProduct(@PathVariable id: Int): ResponseEntity<BaseResponse<ResAllProductDto>>

    @PutMapping("/products/{id}/reduce-stock")
    fun updateStock(
        @PathVariable("id") productId: Int,
        @RequestParam("quantity") quantity: Int
    )
}
