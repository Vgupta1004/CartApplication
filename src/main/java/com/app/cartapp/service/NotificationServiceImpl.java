package com.app.cartapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.cartapp.dto.NotificationRequestDTO;
import com.app.cartapp.dto.NotificationResponseDTO;
import com.app.cartapp.model.Inventory;
import com.app.cartapp.model.Notification;
import com.app.cartapp.model.NotificationStatus;
import com.app.cartapp.repository.InventoryRepository;
import com.app.cartapp.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
    private NotificationRepository notificationRepository;
	
	@Autowired
	private InventoryRepository inventoryRepository;

	private Notification convertToEntity(NotificationRequestDTO dto) {

        Notification notification = new Notification();

        notification.setNotificationType(dto.getNotificationType());
        notification.setMessage(dto.getMessage());
        notification.setStatus(NotificationStatus.UNREAD);
        Inventory inventory = inventoryRepository.findById(dto.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + dto.getInventoryId()));

        notification.setInventory(inventory);
        return notification;
    }

    // Convert Entity → DTO
	private NotificationResponseDTO convertToDTO(Notification entity) {

        return new NotificationResponseDTO(
                entity.getNotificationId(),
                entity.getNotificationType(),
                entity.getMessage(),
                entity.getStatus(),
                entity.getInventory().getInventoryId(),
                entity.getInventory().getProduct().getProductName() 
        );
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO dto) {
        Notification notification = convertToEntity(dto);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return convertToDTO(notification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUser(String userId) {
        return notificationRepository.findByRecipientReference(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
