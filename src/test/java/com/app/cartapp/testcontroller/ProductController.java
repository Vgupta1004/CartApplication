package com.app.cartapp.testcontroller;

import com.app.cartapp.controller.ProductController;
import com.app.cartapp.model.Product;
import com.app.cartapp.service.ProductService;
import com.app.cartapp.exception.ResourceNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(ProductController.class)
@Import(com.app.cartapp.exception.GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    private Product createProduct() {
        Product p = new Product();
        p.setProductId(1L);
        p.setProductName("Laptop");
        p.setDescription("Gaming Laptop");
        p.setCategory("Electronics");
        p.setPrice(75000);
        p.setSku("LAP123");
        return p;
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any()))
                .thenReturn(createProduct());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProduct())))
                .andExpect(status().isOk())  // your controller returns 200, not 201
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }
    @Test
    void testCreateProduct_NotFound() throws Exception {
        when(productService.createProduct(any()))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProduct())))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts())
                .thenReturn(List.of(createProduct()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L));
    }
    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L))
                .thenReturn(createProduct());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L));
    }
    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(1L))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any()))
                .thenReturn(createProduct());

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProduct())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }
    @Test
    void testUpdateProduct_NotFound() throws Exception {
        when(productService.updateProduct(eq(1L), any()))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProduct())))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

}