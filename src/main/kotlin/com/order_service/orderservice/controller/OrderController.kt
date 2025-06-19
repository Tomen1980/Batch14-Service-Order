package com.order_service.orderservice.controller


import com.order_service.orderservice.domain.dto.request.ReqOrderDto
import com.order_service.orderservice.domain.dto.response.BaseResponse
import com.order_service.orderservice.domain.dto.response.ResOrderDto
import com.order_service.orderservice.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/order")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        @RequestBody req: ReqOrderDto
    ): ResponseEntity<BaseResponse<ResOrderDto>> {
        val res = orderService.createOrder(req)
        return ResponseEntity(
            BaseResponse(
                data = res,
                message = "Sukses membuat pesanan"
            ),
            HttpStatus.CREATED
        )
    }

    @GetMapping
    fun getOrderById(): ResponseEntity<BaseResponse<List<ResOrderDto>>>{
        val res = orderService.getOrderByUser()
        return ResponseEntity(
            BaseResponse(
                data = res,
                message = "Sukses menampilkan data order"
            ),
            HttpStatus.OK
        )
    }

    @PutMapping("/{id}")
    fun updateStatus(
        @PathVariable id: Int
    ): ResponseEntity<BaseResponse<String?>>{
        val res = orderService.updateOrder(id)
        return ResponseEntity(
            BaseResponse(
                data = null,
                message = "Sukses menampilkan data order"
            ),
            HttpStatus.CREATED
        )
    }


}