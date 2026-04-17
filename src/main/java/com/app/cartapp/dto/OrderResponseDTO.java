package com.app.cartapp.dto;

import java.util.List;

import com.app.cartapp.model.OrderStatus;

public class OrderResponseDTO {
	private Long orderId;
	private Long userId;
	private Double userAmount;
	private OrderStatus orderStatus;
	private List<OrderItemResponseDTO> orderItems;
}
