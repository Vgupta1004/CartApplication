package com.app.cartapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.cartapp.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
