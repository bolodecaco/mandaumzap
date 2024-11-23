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

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @PatchMapping("/{id}")
    public NotificationDTO updateRead(@PathVariable UUID id, @RequestBody UpdateNotificationReadDTO read) {
        return notificationService.updateRead(id, read.isRead());
    }

    @PostMapping
    public String createNotification(@RequestBody Notification notification) {
        notificationService.createNotification(notification);
        return "Notification created";
    }
}
