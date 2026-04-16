package com.app.cartapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDTO {

    private Long inventoryId;
    private Long productId;
    private String productName;
    private Integer availableQuantity;
    private Integer reorderLevel;
    private Boolean isLowStock;

}
