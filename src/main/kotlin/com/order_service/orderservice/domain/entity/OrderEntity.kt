package com.order_service.orderservice.domain.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "order_number", nullable = false, unique = true)
    val orderNumber: String = UUID.randomUUID().toString(),

    @Column(name = "created_by", nullable = false)
    val createdBy: Int ,

    @Column(name = "status", nullable = false)
    val status: String = "pending",

    @Column(name = "total_price", nullable = false)
    val totalPrice: BigDecimal,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val items: List<OrderItemEntity> = emptyList()
)
