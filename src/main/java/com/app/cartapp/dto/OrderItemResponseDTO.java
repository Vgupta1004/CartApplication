package com.app.cartapp.dto;

public class OrderItemResponseDTO {
	private Long orderItemId;
	private Long productId;
	private String productName;
	private Integer quantity;
	private Double unitPrice;
	
	//Compute
	private Double totalPrice;
}
