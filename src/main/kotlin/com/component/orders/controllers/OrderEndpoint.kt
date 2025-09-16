package com.component.orders.controllers

import com.example.bff.OrderRequest
import com.example.bff.OrderResponse
import com.example.orders.CreateOrder
import com.example.orders.OrderAPIService
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import java.io.File
import java.net.URL


@Endpoint
class OrderEndpoint {

    companion object {
        const val NAMESPACE_URI: String = "http://www.example.com/bff"
        val ORDER_API_WSDL: URL = File("wsdls/order_api.wsdl").toURI().toURL()
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderRequest")
    @ResponsePayload
    fun createOrder(@RequestPayload request: OrderRequest): OrderResponse {
        val createOrder = CreateOrder().apply {
            productid = request.productid
            count = request.count
        }

        val newOrderId = OrderAPIService(ORDER_API_WSDL).orderAPIPort.createOrder(createOrder)

        return OrderResponse().apply {
            id = newOrderId.id
        }
    }
}