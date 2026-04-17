package com.app.cartapp.service;

import com.app.cartapp.dto.InventoryRequestDTO;
import com.app.cartapp.dto.InventoryResponseDTO;
import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.model.Inventory;
import com.app.cartapp.model.Product;
import com.app.cartapp.repository.InventoryRepository;
import com.app.cartapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public InventoryResponseDTO createInventory(InventoryRequestDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + request.getProductId()));

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setReorderLevel(request.getReorderLevel());

        Inventory savedInventory = inventoryRepository.save(inventory);
        return mapToResponseDTO(savedInventory);
    }

    @Override
    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponseDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for Product ID: " + productId));
        return mapToResponseDTO(inventory);
    }

    @Override
    @Transactional
    public InventoryResponseDTO updateInventory(Long productId, InventoryRequestDTO request) {
        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for Product ID: " + productId));

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setReorderLevel(request.getReorderLevel());

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return mapToResponseDTO(updatedInventory);
    }

    @Override
    public List<InventoryResponseDTO> getLowStockInventory() {
        return inventoryRepository.findLowStockInventory()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validateStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for Product ID: " + productId));
        return inventory.getAvailableQuantity() >= quantity;
    }

    @Override
    @Transactional
    public void reduceStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for Product ID: " + productId));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException(
                    "Insufficient stock for Product ID: " + productId);
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void addStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for Product ID: " + productId));

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    private InventoryResponseDTO mapToResponseDTO(Inventory inventory) {
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setInventoryId(inventory.getInventoryId());
        dto.setProductId(inventory.getProduct().getProductId());
        dto.setProductName(inventory.getProduct().getProductName());
        dto.setAvailableQuantity(inventory.getAvailableQuantity());
        dto.setReorderLevel(inventory.getReorderLevel());
        dto.setIsLowStock(inventory.getAvailableQuantity() <= inventory.getReorderLevel());
        return dto;
    }

}
