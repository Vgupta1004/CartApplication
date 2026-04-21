package com.app.cartapp.testservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.app.cartapp.dto.OrderItemRequestDTO;
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
import com.app.cartapp.service.OrderServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);

        product = new Product();
        product.setProductId(101L);
        product.setProductName("Laptop");

        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setAvailableQuantity(50);
    }

    @Test
    void testPlaceOrder_Success() {

        OrderRequestDTO request = TestDataUtil.createOrderRequestDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct_ProductId(101L))
                .thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDTO response = orderService.placeOrder(request);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(2, response.getOrderItems().get(0).getQuantity());

        assertEquals(48, inventory.getAvailableQuantity());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testPlaceOrder_UserNotFound() {

        OrderRequestDTO request = TestDataUtil.createOrderRequestDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(request));
    }

    @Test
    void testPlaceOrder_ProductNotFound() {

        OrderRequestDTO request = TestDataUtil.createOrderRequestDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(request));
    }

    @Test
    void testPlaceOrder_InventoryNotFound() {

        OrderRequestDTO request = TestDataUtil.createOrderRequestDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct_ProductId(101L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(request));
    }

    @Test
    void testPlaceOrder_InsufficientStock() {

        OrderRequestDTO request = TestDataUtil.createOrderRequestDTO();

        inventory.setAvailableQuantity(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct_ProductId(101L))
                .thenReturn(Optional.of(inventory));

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(request));
    }

    @Test
    void testGetOrderById_Success() {

        Order order = TestDataUtil.createOrderEntity(user, product);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDTO response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.getOrderById(1L));
    }

    @Test
    void testGetOrdersByUser() {

        Order order = TestDataUtil.createOrderEntity(user, product);

        when(orderRepository.findByUserUserId(1L))
                .thenReturn(List.of(order));

        List<OrderResponseDTO> list =
                orderService.getOrdersByUser(1L);

        assertEquals(1, list.size());
    }

    @Test
    void testGetRecentOrders() {

        Order order = TestDataUtil.createOrderEntity(user, product);

        when(orderRepository.findTop20ByOrderByOrderIdDesc())
                .thenReturn(List.of(order));

        List<OrderResponseDTO> list =
                orderService.getRecentOrders();

        assertEquals(1, list.size());
    }
}


class TestDataUtil {

    static OrderRequestDTO createOrderRequestDTO() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(1L);

        var item = new OrderItemRequestDTO();
        item.setProductId(101L);
        item.setQuantity(2);
        item.setUnitPrice(50000.0);

        request.setOrderItems(List.of(item));
        return request;
    }

    static Order createOrderEntity(User user, Product product) {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setTotalAmount(100000.0);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(50000.0);

        order.addItem(item);
        return order;
    }
}