package com.app.cartapp.testservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.app.cartapp.dto.NotificationRequestDTO;
import com.app.cartapp.dto.NotificationResponseDTO;
import com.app.cartapp.model.Inventory;
import com.app.cartapp.model.Notification;
import com.app.cartapp.model.NotificationStatus;
import com.app.cartapp.model.Product;
import com.app.cartapp.repository.InventoryRepository;
import com.app.cartapp.repository.NotificationRepository;
import com.app.cartapp.service.NotificationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Inventory inventory;
    private Product product;
    private Notification notification;
    private NotificationRequestDTO request;

    @BeforeEach
    void setUp() {

        product = new Product();
        product.setProductId(101L);
        product.setProductName("Laptop");

        inventory = new Inventory();
        inventory.setInventoryId(10L);
        inventory.setProduct(product);

        notification = new Notification();
        notification.setNotificationId(1L);
        notification.setMessage("Low stock alert");
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setInventory(inventory);

        request = new NotificationRequestDTO();
        request.setInventoryId(10L);
        request.setMessage("Low stock alert");
        request.setNotificationType("LOW_STOCK");
    }

    @Test
    void testCreateNotification_Success() {

        when(inventoryRepository.findById(10L))
                .thenReturn(Optional.of(inventory));
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> {
                    Notification n = invocation.getArgument(0);
                    n.setNotificationId(1L);
                    return n;
                });

        NotificationResponseDTO response =
                notificationService.createNotification(request);

        assertNotNull(response);
        assertEquals(1L, response.getNotificationId());
        assertEquals("Low stock alert", response.getMessage());

        verify(inventoryRepository).findById(10L);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testCreateNotification_InventoryNotFound() {

        when(inventoryRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                notificationService.createNotification(request));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testGetAllNotifications() {

        when(notificationRepository.findAll())
                .thenReturn(List.of(notification));

        List<NotificationResponseDTO> list =
                notificationService.getAllNotifications();

        assertEquals(1, list.size());
        assertEquals("Low stock alert", list.get(0).getMessage());

        verify(notificationRepository).findAll();
    }

    @Test
    void testGetNotificationById_Success() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        NotificationResponseDTO response =
                notificationService.getNotificationById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getNotificationId());
    }

    @Test
    void testGetNotificationById_NotFound() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                notificationService.getNotificationById(1L));
    }

    @Test
    void testGetNotificationsByUser() {

        when(notificationRepository.findByRecipientReference("user1"))
                .thenReturn(List.of(notification));

        List<NotificationResponseDTO> list =
                notificationService.getNotificationsByUser("user1");

        assertEquals(1, list.size());
        assertEquals("Low stock alert", list.get(0).getMessage());

        verify(notificationRepository)
                .findByRecipientReference("user1");
    }
}