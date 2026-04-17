package com.app.cartapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequestDTO {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Available quantity cannot be null")
    @Positive(message = "Available quantity must be greater than 0")
    private Integer availableQuantity;

    @NotNull(message = "Reorder level cannot be null")
    @Positive(message = "Reorder level must be greater than 0")
    private Integer reorderLevel;

}
