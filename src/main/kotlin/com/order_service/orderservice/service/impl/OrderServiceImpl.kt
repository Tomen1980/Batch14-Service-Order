package com.order_service.orderservice.service.impl

import com.order_service.orderservice.domain.dto.constant.Constant
import com.order_service.orderservice.domain.dto.request.ReqOrderDto
import com.order_service.orderservice.domain.dto.response.OrderItemDetail
import com.order_service.orderservice.domain.dto.response.ResOrderDto
import com.order_service.orderservice.domain.entity.OrderEntity
import com.order_service.orderservice.domain.entity.OrderItemEntity
import com.order_service.orderservice.exception.CustomException
import com.order_service.orderservice.repository.OrderItemRepository
import com.order_service.orderservice.repository.OrderRepository
import com.order_service.orderservice.rest.ProductManagementClient
import org.springframework.stereotype.Service
import com.order_service.orderservice.service.OrderService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class OrderServiceImpl (
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val productManagementClient: ProductManagementClient,
    private val httpServletRequest: HttpServletRequest
): OrderService{

    @Transactional
    override fun createOrder(req: ReqOrderDto) : ResOrderDto {
        var userId = httpServletRequest.getHeader(Constant.HEADER_USER_ID)
        var role = httpServletRequest.getHeader(Constant.HEADER_USER_ROLE)
        var totalPrice = BigDecimal.ZERO

        if(role != "user"){
            throw CustomException("Tidak bisa membuat pesanan selain user", 403)
        }

        // Ambil data produk dari product service & hitung total
        val itemDetails = req.items.map { item ->
            val response = productManagementClient.getProduct(item.productId)
            println(response)
            val product = response.body?.data
                ?: throw CustomException("Produk dengan ID ${item.productId} tidak ditemukan", 404)

            val subtotal = product.price.multiply(BigDecimal(item.quantity))
            totalPrice += subtotal

            Triple(item, product.price, product.name)
        }

        // Simpan entitas Order
        val orderEntity = OrderEntity(
            createdBy = userId.toInt(),
            totalPrice = totalPrice
        )
        val savedOrder = orderRepository.save(orderEntity)

        // Simpan entitas OrderItem
        val orderItems = itemDetails.map { (item, price, productName) ->
            OrderItemEntity(
                order = savedOrder,
                productId = item.productId,
                productName = productName,
                quantity = item.quantity,
                price = price,
                createdAt = LocalDateTime.now()
            )
        }
        orderItemRepository.saveAll(orderItems)

        // Return response DTO
        return ResOrderDto(
            orderId = savedOrder.id.toLong(),
            totalAmount = savedOrder.totalPrice,
            orderNumber = savedOrder.orderNumber,
            status = savedOrder.status,
            items = orderItems.map {
                OrderItemDetail(
                    productId = it.productId,
                    quantity = it.quantity,
                    producName = it.productName.toString(),
                    price = it.price
                )
            }
        )
    }

    @GetMapping
    override fun getOrderByUser():  List<ResOrderDto> {
        val userId = httpServletRequest.getHeader(Constant.HEADER_USER_ID)?.toIntOrNull()
            ?: throw CustomException("User ID tidak valid", 400)
        val role = httpServletRequest.getHeader(Constant.HEADER_USER_ROLE)
        if (role != "user") throw CustomException("Hanya user yang bisa akses", 403)

        val results = orderRepository.getOrder(userId)

        if (results.isEmpty()) throw CustomException("Order tidak ditemukan", 404)

        // Grouping berdasarkan order_id
        val grouped = results.groupBy { (it["order_id"] as Number).toLong() }

        return grouped.map { (orderId, rows) ->
            val first = rows.first()
            ResOrderDto(
                orderId = orderId,
                orderNumber = first["order_number"] as String,
                totalAmount = first["total_amount"] as BigDecimal,
                status = first["status"] as String,
                items = rows.map {
                    OrderItemDetail(
                        productId = (it["product_id"] as Number).toInt(),
                        quantity = (it["quantity"] as Number).toInt(),
                        producName = (it["productName"]).toString(),
                        price = it["price"] as BigDecimal
                    )
                }
            )
        }
    }

    @Transactional
    override fun updateOrder(orderId: Int) {
        val order = orderRepository.findById(orderId).orElseThrow {
            CustomException("Order tidak ditemukan", 404)
        }

        if (order.status == "lunas") {
            throw CustomException("Order sudah lunas", 400)
        }


        // Update status
        val updatedOrder = order.copy(
            status = "lunas",
            updatedAt = LocalDateTime.now()
        )
        orderRepository.save(updatedOrder)
        // Jika status menjadi paid, kurangi stok produk
        if (order.status == "lunas") {
            for (item in order.items) {
                productManagementClient.updateStock(
                    productId = item.productId,
                    quantity = item.quantity
                )
            }
        }
    }
}