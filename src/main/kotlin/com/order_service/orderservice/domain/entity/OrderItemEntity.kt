package com.order_service.orderservice.domain.entity
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "order_items")
data class OrderItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: OrderEntity?,

    @Column(name = "product_id", nullable = false)
    val productId: Int,

    @Column(name = "product_name", nullable = true)
    val productName: String? = null,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Column(name = "price", nullable = false)
    val price: BigDecimal,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
