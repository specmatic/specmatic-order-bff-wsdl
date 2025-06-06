package com.component.orders.controllers

import com.example.orders.CreateOrder
import com.example.orders.OrderAPIService
import com.example.orders.OrderRequest
import com.example.orders.OrderResponse
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import java.io.File


@Endpoint
class OrderEndpoint {

    companion object {
        const val NAMESPACE_URI: String = "http://www.example.com/orders"
        val ORDER_API_WSDL = File("wsdls/order_api.wsdl").toURI().toURL()
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderRequest")
    @ResponsePayload
    fun createOrder(@RequestPayload request: OrderRequest): OrderResponse {
        val createOrder = CreateOrder().apply {
            setProductid(request.productid)
            setCount(request.count)
        }

        val newOrderId = OrderAPIService(ORDER_API_WSDL).orderAPIPort.createOrder(createOrder)

        return OrderResponse().apply {
            id = newOrderId.id
        }
    }
}