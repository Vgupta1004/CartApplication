package com.app.cartapp.controller;

import com.app.cartapp.dto.InventoryRequestDTO;
import com.app.cartapp.dto.InventoryResponseDTO;
import com.app.cartapp.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Create new inventory for a product
     * POST /api/inventory
     */
    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(
            @Valid @RequestBody InventoryRequestDTO request) {
        return new ResponseEntity<>(
                inventoryService.createInventory(request),
                HttpStatus.CREATED
        );
    }

    /**
     * Get all inventory
     * GET /api/inventory
     */
    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        return new ResponseEntity<>(
                inventoryService.getAllInventory(),
                HttpStatus.OK
        );
    }

    /**
     * Get inventory by product ID
     * GET /api/inventory/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryByProductId(
            @PathVariable Long productId) {
        return new ResponseEntity<>(
                inventoryService.getInventoryByProductId(productId),
                HttpStatus.OK
        );
    }

    /**
     * Update inventory by product ID
     * PUT /api/inventory/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryRequestDTO request) {
        return new ResponseEntity<>(
                inventoryService.updateInventory(productId, request),
                HttpStatus.OK
        );
    }

    /**
     * Get low stock inventory
     * GET /api/inventory/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponseDTO>> getLowStockInventory() {
        return new ResponseEntity<>(
                inventoryService.getLowStockInventory(),
                HttpStatus.OK
        );
    }

}
