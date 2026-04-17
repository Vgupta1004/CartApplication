package com.app.cartapp.dto;

import com.app.cartapp.model.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
	private Long notificationId;
    private String notificationType;
    private String message;
    private NotificationStatus status;

    private Long inventoryId;
    private String productName;
}
