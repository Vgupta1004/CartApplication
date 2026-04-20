package com.app.cartapp.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.cartapp.dto.NotificationRequestDTO;
import com.app.cartapp.dto.NotificationResponseDTO;
import com.app.cartapp.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // POST /api/notifications
    @PostMapping
    public NotificationResponseDTO createNotification(@RequestBody NotificationRequestDTO dto) {
        return notificationService.createNotification(dto);
    }

    // GET /api/notifications
    @GetMapping
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    // GET /api/notifications/{id}
    @GetMapping("/{id}")
    public NotificationResponseDTO getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    // GET /api/notifications/user/{userId}
    @GetMapping("/user/{userId}")
    public List<NotificationResponseDTO> getNotificationsByUser(@PathVariable String userId) {
        return notificationService.getNotificationsByUser(userId);
    }
}