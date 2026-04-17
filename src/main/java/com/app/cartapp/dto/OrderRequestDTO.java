package com.app.cartapp.dto;

import java.util.List;

import com.app.cartapp.model.OrderStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequestDTO {
	@NotNull(message = "user id cannot be null")
	private Long userId;
	
	private List<OrderItemRequestDTO> orderItems;
	@NotNull(message = "total amount cannot be null")
	@Positive(message = "total amount must be greater than zero")
	private Double totalAmount;
	
	@NotNull(message = "Order Status cannot be null")
	private OrderStatus orderStatus;

}
