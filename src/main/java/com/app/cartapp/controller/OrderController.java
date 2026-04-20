package com.app.cartapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.cartapp.dto.OrderRequestDTO;
import com.app.cartapp.dto.OrderResponseDTO;
import com.app.cartapp.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // POST /api/checkout
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO request) {
        OrderResponseDTO response = orderService.placeOrder(request);
        return ResponseEntity.ok(response);
    }

    // GET /api/orders/{id}
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    // GET /api/orders
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getRecentOrders() {
        List<OrderResponseDTO> orders = orderService.getRecentOrders();
        return ResponseEntity.ok(orders);
    }

    // GET /api/orders/user/{userId}
    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(
            @PathVariable Long userId) {

        List<OrderResponseDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }
}