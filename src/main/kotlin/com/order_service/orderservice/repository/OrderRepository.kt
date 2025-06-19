package com.order_service.orderservice.repository

import com.order_service.orderservice.domain.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OrderRepository: JpaRepository<OrderEntity, Int>{
    @Query(
        """
        SELECT 
            o.id AS order_id,
            o.status AS status,
            o.order_number AS order_number,
            o.created_by AS user_id,
            o.total_price AS total_amount,
            oi.product_id AS product_id,
            oi.product_name AS product_name,
            oi.quantity AS quantity,
            oi.price AS price
        FROM orders o
        LEFT JOIN order_items oi ON o.id = oi.order_id
        WHERE o.created_by = :userId
        """,
        nativeQuery = true
    )
    fun getOrder(@Param("userId") userId: Int): List<Map<String, Any>>


}