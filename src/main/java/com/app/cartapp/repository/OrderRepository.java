package com.app.cartapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.cartapp.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	 List<Order> findByUserUserId(Long userId);
	 List<Order> findTop20ByOrderByOrderIdDesc();
}
