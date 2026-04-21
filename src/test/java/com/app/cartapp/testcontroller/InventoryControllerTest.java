package com.app.cartapp.testcontroller;
 
import com.app.cartapp.controller.InventoryController;
import com.app.cartapp.dto.InventoryRequestDTO;
import com.app.cartapp.dto.InventoryResponseDTO;
import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
 
import java.util.List;
 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
@WebMvcTest(InventoryController.class)
@Import(com.app.cartapp.exception.GlobalExceptionHandler.class)
class InventoryControllerTest {
 
    @Autowired
    private MockMvc mockMvc;
 
    private final ObjectMapper objectMapper = new ObjectMapper();
 
    @MockitoBean
    private InventoryService inventoryService;
 
    private InventoryRequestDTO createRequest() {
        InventoryRequestDTO req = new InventoryRequestDTO();
        req.setProductId(1L);
        req.setAvailableQuantity(50);
        req.setReorderLevel(10);
        return req;
    }
 
    private InventoryResponseDTO createResponse() {
        InventoryResponseDTO res = new InventoryResponseDTO();
        res.setProductId(1L);
        res.setProductName("Laptop");
        res.setAvailableQuantity(50);
        res.setReorderLevel(10);
        res.setIsLowStock(false);
        return res;
    }
 
    @Test
    void testCreateInventory() throws Exception {
        when(inventoryService.createInventory(any()))
                .thenReturn(createResponse());
 
        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }
 
    @Test
    void testCreateInventory_NotFound() throws Exception {
        when(inventoryService.createInventory(any()))
                .thenThrow(new ResourceNotFoundException("Product not found"));
 
        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }
 
    @Test
    void testGetAllInventory() throws Exception {
        when(inventoryService.getAllInventory())
                .thenReturn(List.of(createResponse()));
 
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L));
    }
 
    @Test
    void testGetByProductId() throws Exception {
        when(inventoryService.getInventoryByProductId(1L))
                .thenReturn(createResponse());
 
        mockMvc.perform(get("/api/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L));
    }
 
    @Test
    void testGetByProductId_NotFound() throws Exception {
        when(inventoryService.getInventoryByProductId(1L))
                .thenThrow(new ResourceNotFoundException("Not found"));
 
        mockMvc.perform(get("/api/inventory/1"))
                .andExpect(status().isNotFound());
    }
 
    @Test
    void testUpdateInventory() throws Exception {
        when(inventoryService.updateInventory(eq(1L), any()))
                .thenReturn(createResponse());
 
        mockMvc.perform(put("/api/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }
 
    @Test
    void testUpdateInventory_NotFound() throws Exception {
        when(inventoryService.updateInventory(eq(1L), any()))
                .thenThrow(new ResourceNotFoundException("Not found"));
 
        mockMvc.perform(put("/api/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isNotFound());
    }
 
    @Test
    void testLowStock() throws Exception {
        InventoryResponseDTO res = createResponse();
        res.setIsLowStock(true);
 
        when(inventoryService.getLowStockInventory())
                .thenReturn(List.of(res));
 
        mockMvc.perform(get("/api/inventory/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isLowStock").value(true));
    }
}
 