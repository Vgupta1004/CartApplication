package com.app.cartapp.dto;

import lombok.Data;

@Data
public class OrderItemResponseDTO {
	private Long orderItemId;
	private Long productId;
	private String productName;
	private Integer quantity;
	private Double unitPrice;
	
	//Compute
	private Double totalPrice;
}
