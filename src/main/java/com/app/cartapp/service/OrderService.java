package com.app.cartapp.service;

import java.util.List;

import com.app.cartapp.dto.OrderRequestDTO;
import com.app.cartapp.dto.OrderResponseDTO;

public interface OrderService {
	OrderResponseDTO placeOrder(OrderRequestDTO request);

    OrderResponseDTO getOrderById(Long orderId);

    List<OrderResponseDTO> getOrdersByUser(Long userId);
    
    List<OrderResponseDTO> getRecentOrders();

}
