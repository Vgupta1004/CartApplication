package com.app.cartapp.testcontroller;

import com.app.cartapp.controller.OrderController;
import com.app.cartapp.dto.*;
import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.model.OrderStatus;
import com.app.cartapp.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(com.app.cartapp.exception.GlobalExceptionHandler.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ✅ avoid your ObjectMapper issue
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private OrderService orderService;

    // 🔧 Build Request
    private OrderRequestDTO createRequest() {
        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setProductId(101L);
        item.setQuantity(2);
        item.setUnitPrice(500.0);

        OrderRequestDTO req = new OrderRequestDTO();
        req.setUserId(1L);
        req.setOrderItems(List.of(item));
        req.setTotalAmount(1000.0);
        req.setOrderStatus(OrderStatus.CREATED);

        return req;
    }

    // 🔧 Build Response
    private OrderResponseDTO createResponse() {
        OrderItemResponseDTO item = new OrderItemResponseDTO();
        item.setOrderItemId(1L);
        item.setProductId(101L);
        item.setProductName("Laptop");
        item.setQuantity(2);
        item.setUnitPrice(500.0);
        item.setTotalPrice(1000.0);

        OrderResponseDTO res = new OrderResponseDTO();
        res.setOrderId(1L);
        res.setUserId(1L);
        res.setUserAmount(1000.0);
        res.setOrderStatus(OrderStatus.CREATED);
        res.setOrderItems(List.of(item));

        return res;
    }

    // ✅ PLACE ORDER
    @Test
    void testPlaceOrder() throws Exception {
        when(orderService.placeOrder(any()))
                .thenReturn(createResponse());

        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("CREATED"))
                .andExpect(jsonPath("$.orderItems[0].productId").value(101L));
    }

    // ❌ VALIDATION FAIL (important for your DTO)
    @Test
    void testPlaceOrder_ValidationFail() throws Exception {
        OrderRequestDTO invalid = new OrderRequestDTO(); // empty

        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    // ✅ GET ORDER BY ID
    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L))
                .thenReturn(createResponse());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.orderItems[0].productName").value("Laptop"));
    }

    // ❌ NOT FOUND
    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.getOrderById(1L))
                .thenThrow(new ResourceNotFoundException("Order not found"));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    // ✅ GET ALL
    @Test
    void testGetRecentOrders() throws Exception {
        when(orderService.getRecentOrders())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1L));
    }

    // ✅ GET BY USER
    @Test
    void testGetOrdersByUser() throws Exception {
        when(orderService.getOrdersByUser(1L))
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));
    }
}