package com.app.cartapp.dto;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {

    @NotBlank
    private String productName;

    private String description;

    @NotBlank
    private String category;

    @NotNull
    @Positive
    private Double price;

    @NotBlank
    private String sku;
}