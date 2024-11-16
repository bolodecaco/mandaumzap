package com.server.demo.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.services.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications().stream()
                .map(notification -> new NotificationDTO(
                        notification.getId(),
                        notification.getContent(),
                        notification.isRead(),
                        notification.getType()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public NotificationDTO getNotificationById(UUID id) {
        return notificationService.getNotificationById(id);
    }


    @PatchMapping("/{id}")
    public NotificationDTO updateRead(UUID id, boolean read) {
        return notificationService.updateRead(id, read);
    }

}
