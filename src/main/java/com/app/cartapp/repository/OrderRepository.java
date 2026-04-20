package com.app.cartapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.cartapp.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	findByUserUserId
}
