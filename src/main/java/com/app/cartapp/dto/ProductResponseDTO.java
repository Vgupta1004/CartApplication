package com.app.cartapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDTO {

    private Long productId;
    private String productName;
    private String description;
    private String category;
    private Double price;
    private String sku;
}