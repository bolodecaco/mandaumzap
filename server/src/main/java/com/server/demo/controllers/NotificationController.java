package com.server.demo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.UpdateNotificationReadDTO;
import com.server.demo.models.Notification;
import com.server.demo.services.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Notification API")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Retorna todas as notificações")
    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @Operation(summary = "Atualiza o status de leitura de uma notificação")
    @PatchMapping("/{id}")
    public NotificationDTO updateRead(@PathVariable UUID id, @RequestBody UpdateNotificationReadDTO read) {
        return notificationService.updateRead(id, read.isRead());
    }

    @Operation(summary = "Cria uma nova notificação")
    @PostMapping
    public String createNotification(@RequestBody Notification notification) {
        notificationService.createNotification(notification);
        return "Notification created";
    }
}
