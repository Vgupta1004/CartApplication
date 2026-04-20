package com.app.cartapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    private String notificationType;
    private String recipientReference;
    private String message;
    
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;  
    
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
}
