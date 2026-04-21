package com.app.cartapp.testcontroller;

import com.app.cartapp.controller.NotificationController;
import com.app.cartapp.dto.NotificationRequestDTO;
import com.app.cartapp.dto.NotificationResponseDTO;
import com.app.cartapp.model.NotificationStatus;
import com.app.cartapp.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private NotificationService notificationService;

    // Request DTO
    private NotificationRequestDTO createRequest() {
        NotificationRequestDTO req = new NotificationRequestDTO();
        req.setNotificationType("LOW_STOCK");
        req.setMessage("Stock is low");
        req.setInventoryId(1L);
        return req;
    }

    // Response DTO (UPDATED)
    private NotificationResponseDTO createResponse() {
        return new NotificationResponseDTO(
                1L,
                "LOW_STOCK",
                "Stock is low",
                NotificationStatus.UNREAD,   
                1L,
                "Laptop"                     
        );
    }

    //  CREATE
    @Test
    void testCreateNotification() throws Exception {

        when(notificationService.createNotification(any()))
                .thenReturn(createResponse());

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationType").value("LOW_STOCK"))
                .andExpect(jsonPath("$.status").value("UNREAD")) // enum serialized as string
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }

    //  GET ALL
    @Test
    void testGetAllNotifications() throws Exception {

        when(notificationService.getAllNotifications())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationId").value(1L))
                .andExpect(jsonPath("$[0].status").value("UNREAD"));
    }

    //  GET BY ID
    @Test
    void testGetNotificationById() throws Exception {

        when(notificationService.getNotificationById(1L))
                .thenReturn(createResponse());

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }
    
    //  GET BY USER
    @Test
    void testGetNotificationsByUser() throws Exception {

        when(notificationService.getNotificationsByUser("user123"))
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/notifications/user/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("UNREAD"));
    }
}