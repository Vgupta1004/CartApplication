package com.app.cartapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequestDTO {

	@NotNull(message = "product id cannot be null")
	private Long productId;
	
	@NotNull(message = "quantity cannot be null")
	@Positive(message = "quantity must be greater than zero")
	private Integer quantity;
	
	@NotNull(message = "unit price cannot be null")
	@Positive(message = "unit price must be greater than 0")
	private Double unitPrice;
}
