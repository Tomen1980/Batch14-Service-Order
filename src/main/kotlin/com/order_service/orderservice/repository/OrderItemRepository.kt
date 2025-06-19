package com.order_service.orderservice.repository
import com.order_service.orderservice.domain.entity.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository: JpaRepository<OrderItemEntity, Int> {

}