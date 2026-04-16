package com.app.cartapp.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.model.Product;
import com.app.cartapp.repository.ProductRepository;

import jakarta.persistence.*;
@Service
public class ProductService {
	@Autowired
	private ProductRepository repo;
	public Product createProduct(Product product) {
		return repo.save(product);
	}
	public List<Product> getAllProducts(){
		return repo.findAll();
	}
	public Product getProductById(Long id) {
	    return repo.findById(id)
	        .orElseThrow(() -> 
	            new ResourceNotFoundException("Product not found with id " + id)
	        );
	}
	public Product updateProduct(Long id, Product updatedProduct){
		Product product=getProductById(id);
		product.setProductName(updatedProduct.getProductName());
		product.setDescription(updatedProduct.getDescription());
		product.setCategory(updatedProduct.getCategory());
		product.setPrice(updatedProduct.getPrice());
		product.setSku(updatedProduct.getSku());
		return repo.save(product);
	}
	public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}