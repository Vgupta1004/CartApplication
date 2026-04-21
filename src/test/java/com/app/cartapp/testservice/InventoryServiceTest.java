package com.app.cartapp.testservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.app.cartapp.dto.InventoryRequestDTO;
import com.app.cartapp.dto.InventoryResponseDTO;
import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.model.Inventory;
import com.app.cartapp.model.Product;
import com.app.cartapp.repository.InventoryRepository;
import com.app.cartapp.repository.ProductRepository;
import com.app.cartapp.service.InventoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Product product;
    private Inventory inventory;
    private InventoryRequestDTO request;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");

        inventory = new Inventory();
        inventory.setInventoryId(10L);
        inventory.setProduct(product);
        inventory.setAvailableQuantity(50);
        inventory.setReorderLevel(10);

        request = new InventoryRequestDTO();
        request.setProductId(1L);
        request.setAvailableQuantity(50);
        request.setReorderLevel(10);
    }

    @Test
    void testCreateInventory_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        InventoryResponseDTO response = inventoryService.createInventory(request);

        assertNotNull(response);
        assertEquals(1L, response.getProductId());
        assertEquals("Laptop", response.getProductName());

        verify(productRepository).findById(1L);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void testCreateInventory_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                inventoryService.createInventory(request));

        verify(productRepository).findById(1L);
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void testGetAllInventory() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));

        List<InventoryResponseDTO> list = inventoryService.getAllInventory();

        assertEquals(1, list.size());
        assertEquals("Laptop", list.get(0).getProductName());

        verify(inventoryRepository).findAll();
    }

    @Test
    void testGetInventoryByProductId_Success() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.of(inventory));

        InventoryResponseDTO response =
                inventoryService.getInventoryByProductId(1L);

        assertNotNull(response);
        assertEquals(50, response.getAvailableQuantity());

        verify(inventoryRepository).findByProduct_ProductId(1L);
    }

    @Test
    void testGetInventoryByProductId_NotFound() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                inventoryService.getInventoryByProductId(1L));
    }

    @Test
    void testUpdateInventory_Success() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(inventory);

        request.setAvailableQuantity(30);
        request.setReorderLevel(5);

        InventoryResponseDTO response =
                inventoryService.updateInventory(1L, request);

        assertEquals(30, inventory.getAvailableQuantity());
        assertEquals(5, inventory.getReorderLevel());

        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testUpdateInventory_NotFound() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                inventoryService.updateInventory(1L, request));
    }

    @Test
    void testGetLowStockInventory() {
        inventory.setAvailableQuantity(5);

        when(inventoryRepository.findLowStockInventory())
                .thenReturn(List.of(inventory));

        List<InventoryResponseDTO> list =
                inventoryService.getLowStockInventory();

        assertTrue(list.get(0).getIsLowStock());
    }

    @Test
    void testValidateStock_True() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.of(inventory));

        boolean result = inventoryService.validateStock(1L, 20);

        assertTrue(result);
    }

    @Test
    void testValidateStock_False() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.of(inventory));

        boolean result = inventoryService.validateStock(1L, 100);

        assertFalse(result);
    }

    @Test
    void testReduceStock_Success() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.of(inventory));

        inventoryService.reduceStock(1L, 10);

        assertEquals(40, inventory.getAvailableQuantity());
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testReduceStock_Insufficient() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.of(inventory));

        assertThrows(RuntimeException.class, () ->
                inventoryService.reduceStock(1L, 100));
    }

    @Test
    void testAddStock() {
        when(inventoryRepository.findByProduct_ProductId(1L))
                .thenReturn(Optional.of(inventory));

        inventoryService.addStock(1L, 20);

        assertEquals(70, inventory.getAvailableQuantity());
        verify(inventoryRepository).save(inventory);
    }
}