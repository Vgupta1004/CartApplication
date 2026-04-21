package com.app.cartapp.testservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.model.Product;
import com.app.cartapp.repository.ProductRepository;
import com.app.cartapp.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");
        product.setDescription("Gaming Laptop");
        product.setCategory("Electronics");
        product.setPrice(75000.0);
        product.setSku("SKU123");
    }

    @Test
    void testCreateProduct() {
        when(repo.save(any(Product.class))).thenReturn(product);

        Product saved = productService.createProduct(product);

        assertNotNull(saved);
        assertEquals("Laptop", saved.getProductName());
        verify(repo, times(1)).save(product);
    }

    @Test
    void testGetAllProducts() {
        when(repo.findAll()).thenReturn(Arrays.asList(product));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Laptop", products.get(0).getProductName());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGetProductById_Success() {
        when(repo.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testUpdateProduct_Success() {
        Product updated = new Product();
        updated.setProductName("Updated Laptop");
        updated.setDescription("Updated Desc");
        updated.setCategory("Electronics");
        updated.setPrice(80000.0);
        updated.setSku("SKU999");

        when(repo.findById(1L)).thenReturn(Optional.of(product));
        when(repo.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, updated);

        assertNotNull(result);
        assertEquals("Updated Laptop", product.getProductName());
        assertEquals("Updated Desc", product.getDescription());

        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_NotFound() {
        Product updated = new Product();

        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(1L, updated);
        });

        verify(repo, times(1)).findById(1L);
        verify(repo, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(repo).deleteById(1L);

        productService.deleteProduct(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}