package com.app.cartapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.cartapp.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long>{
	
}
