package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.models.Notification;
import com.server.demo.repositories.NotificationRepository;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(
                        notification -> new NotificationDTO(
                                notification.getId(),
                                notification.getContent(),
                                notification.isRead(),
                                notification.getType()
                        )
                ).collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        return new NotificationDTO(notification.getId(), notification.getContent(), notification.isRead(), notification.getType());
    }

    public NotificationDTO createNotification(Notification notification) {
        Notification currentNotification = notificationRepository.save(notification);
        return new NotificationDTO(currentNotification.getId(), currentNotification.getContent(), currentNotification.isRead(), currentNotification.getType());
    }

    public NotificationDTO updateRead(UUID id, boolean read) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(read);
        Notification updatedNotification = notificationRepository.save(notification);
        return new NotificationDTO(updatedNotification.getId(), updatedNotification.getContent(), updatedNotification.isRead(), updatedNotification.getType());
    }

    public NotificationDTO deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
        return new NotificationDTO(notification.getId(), notification.getContent(), notification.isRead(), notification.getType());
    }
}
