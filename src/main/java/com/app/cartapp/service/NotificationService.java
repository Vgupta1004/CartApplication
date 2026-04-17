package com.app.cartapp.service;

import java.util.List;

import com.app.cartapp.dto.NotificationRequestDTO;
import com.app.cartapp.dto.NotificationResponseDTO;

public interface NotificationService {
	
	NotificationResponseDTO createNotification(NotificationRequestDTO dto);
    List<NotificationResponseDTO> getAllNotifications();
    NotificationResponseDTO getNotificationById(Long id);
    List<NotificationResponseDTO> getNotificationsByUser(String userId);
}
