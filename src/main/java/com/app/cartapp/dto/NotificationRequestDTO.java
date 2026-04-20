package com.app.cartapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

	    private String notificationType;
	    private String message;
	    private String status;

	    private Long inventoryId; 
}
