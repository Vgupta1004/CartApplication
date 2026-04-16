package com.app.cartapp.service;

import com.app.cartapp.dto.InventoryRequestDTO;
import com.app.cartapp.dto.InventoryResponseDTO;

import java.util.List;

public interface InventoryService {

    InventoryResponseDTO createInventory(InventoryRequestDTO request);

    List<InventoryResponseDTO> getAllInventory();

    InventoryResponseDTO getInventoryByProductId(Long productId);

    InventoryResponseDTO updateInventory(Long productId, InventoryRequestDTO request);

    List<InventoryResponseDTO> getLowStockInventory();

    boolean validateStock(Long productId, Integer quantity);

    void reduceStock(Long productId, Integer quantity);

    void addStock(Long productId, Integer quantity);

}
