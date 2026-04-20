package com.app.cartapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.cartapp.dto.OrderItemResponseDTO;
import com.app.cartapp.dto.OrderRequestDTO;
import com.app.cartapp.dto.OrderResponseDTO;
import com.app.cartapp.model.Inventory;
import com.app.cartapp.model.Order;
import com.app.cartapp.model.OrderItem;
import com.app.cartapp.model.Product;
import com.app.cartapp.model.User;
import com.app.cartapp.repository.InventoryRepository;
import com.app.cartapp.repository.OrderItemRepository;
import com.app.cartapp.repository.OrderRepository;
import com.app.cartapp.repository.ProductRepository;
import com.app.cartapp.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Override
	public OrderResponseDTO placeOrder(OrderRequestDTO request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User Not found"));
		
		Order order = new Order();
		order.setUser(user);
		order.setOrderStatus(request.getOrderStatus());
		
		List<OrderItem> orderItems = request.getOrderItems().stream().map(itemReq -> {
			Product product = productRepository.findById(itemReq.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
			
			Inventory inventory = inventoryRepository.findByProduct_ProductId(itemReq.getProductId())
					.orElseThrow(() -> new RuntimeException("Inventory not found"));
			
			if (inventory.getAvailableQuantity() < itemReq.getQuantity()) {
				throw new RuntimeException("Insufficient stock");
			}
			
			inventory.setAvailableQuantity(inventory.getAvailableQuantity() - itemReq.getQuantity());
			
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			orderItem.setQuantity(itemReq.getQuantity());
			orderItem.setUnitPrice(itemReq.getUnitPrice());
			
			order.addItem(orderItem);
			return orderItem;
		}).collect(Collectors.toList());
		
		double totalAmount = orderItems.stream().mapToDouble(i -> i.getUnitPrice() * i.getQuantity()).sum();
		order.setTotalAmount(totalAmount);
		Order savedOrder = orderRepository.save(order);
		
		return mapToDTO(savedOrder);
	}
	
	@Override
	public OrderResponseDTO getOrderById(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		return mapToDTO(order);
	}
	
	@Override
	public List<OrderResponseDTO> getOrdersByUser(Long userId) {
		return orderRepository.findByUserUserId(userId).stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	@Override
	public List<OrderResponseDTO> getRecentOrders() {
	    return orderRepository.findTop20ByOrderByOrderIdDesc()
	            .stream()
	            .map(this::mapToDTO)
	            .collect(Collectors.toList());
	}
	
	
	// MAPPINGS
	
	public OrderResponseDTO mapToDTO(Order order) {
	    if (order == null) {
	        return null;
	    }

	    OrderResponseDTO dto = new OrderResponseDTO();
	    dto.setOrderId(order.getOrderId());
	    dto.setUserId(order.getUser().getUserId());
	    dto.setUserAmount(order.getTotalAmount());
	    dto.setOrderStatus(order.getOrderStatus());

	    if (order.getItems() != null) {
	        List<OrderItemResponseDTO> itemDTOs = order.getItems()
	                .stream()
	                .map(this::mapOrderItemToDTO)
	                .collect(Collectors.toList());
	        dto.setOrderItems(itemDTOs);
	    }

	    return dto;
	}
	
	private OrderItemResponseDTO mapOrderItemToDTO(OrderItem item) {
	    OrderItemResponseDTO dto = new OrderItemResponseDTO();
	    dto.setProductId(item.getProduct().getProductId());
	    dto.setQuantity(item.getQuantity());
	    dto.setUnitPrice(item.getUnitPrice());
	    return dto;
	}
}
