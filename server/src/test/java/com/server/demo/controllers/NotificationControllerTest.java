package com.server.demo.controllers;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.config.SecurityTestConfig;
import com.server.demo.controllers.user.NotificationController;
import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.RequestNotificationDTO;
import com.server.demo.dtos.UpdateNotificationReadDTO;
import com.server.demo.services.JwtService;
import com.server.demo.services.NotificationService;

@WebMvcTest(controllers = NotificationController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private NotificationDTO notificationDTO;
    private RequestNotificationDTO requestNotificationDTO;
    private UpdateNotificationReadDTO updateNotificationReadDTO;

    @BeforeEach
    void setUp() {
        notificationDTO = new NotificationDTO();
        notificationDTO.setId(UUID.randomUUID());
        notificationDTO.setContent("Teste de Notificação");
        notificationDTO.setType("TEST");
        notificationDTO.setRead(false);
        notificationDTO.setReceiverId(UUID.randomUUID());

        requestNotificationDTO = new RequestNotificationDTO();
        requestNotificationDTO.setContent("Nova Notificação");
        requestNotificationDTO.setType("TEST");
        requestNotificationDTO.setReceiverId("user-123");

        updateNotificationReadDTO = new UpdateNotificationReadDTO();
        updateNotificationReadDTO.setRead(true);

        when(jwtService.getCurrentUserId()).thenReturn("testUserId");
    }

    @Test
    void shouldGetAllNotifications() throws Exception {
        when(notificationService.getNotificationByReceiverId(anyString()))
            .thenReturn(Arrays.asList(notificationDTO));

        mockMvc.perform(get("/api/user/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notificationDTO.getId().toString()))
                .andExpect(jsonPath("$[0].content").value(notificationDTO.getContent()))
                .andExpect(jsonPath("$[0].type").value(notificationDTO.getType()));
    }

    @Test
    void shouldCreateNotification() throws Exception {
        when(notificationService.createNotification(any(RequestNotificationDTO.class)))
            .thenReturn(notificationDTO);

        mockMvc.perform(post("/api/user/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestNotificationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notificationDTO.getId().toString()))
                .andExpect(jsonPath("$.content").value(notificationDTO.getContent()))
                .andExpect(jsonPath("$.type").value(notificationDTO.getType()));
    }

    @Test
    void shouldUpdateNotificationRead() throws Exception {
        when(notificationService.updateRead(any(UUID.class), any(Boolean.class), anyString()))
            .thenReturn(notificationDTO);

        mockMvc.perform(patch("/api/user/notifications/" + notificationDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateNotificationReadDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notificationDTO.getId().toString()));
    }

    @Test
    void shouldValidateNotificationCreation() throws Exception {
        RequestNotificationDTO invalidNotification = new RequestNotificationDTO();
        invalidNotification.setContent("a"); // Conteúdo muito curto
        invalidNotification.setType("a"); // Tipo muito curto

        mockMvc.perform(post("/api/user/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidNotification)))
                .andExpect(status().isBadRequest());
    }
}
