package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.handlers.NotificationHandler;
import com.server.demo.mappers.NotificationMapper;
import com.server.demo.models.Notification;
import com.server.demo.repositories.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationHandler notificationHandler;

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationByReceiverId(UUID receiverId) {
        return notificationRepository.findByReceiverId(receiverId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void createNotification(Notification notification) {
        UUID receiverId = notification.getReceiver().getId();
        notificationRepository.save(notification);
        this.sendNotification(receiverId);
    }

    public NotificationDTO updateRead(UUID id, boolean read) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        UUID receiverId = notification.getReceiver().getId();
        notification.setRead(read);
        Notification updatedNotification = notificationRepository.save(notification);
        this.sendNotification(receiverId);
        return notificationMapper.toDTO(updatedNotification);
    }

    public NotificationDTO deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        UUID receiverId = notification.getReceiver().getId();
        notificationRepository.delete(notification);
        this.sendNotification(receiverId);
        return notificationMapper.toDTO(notification);
    }

    public List<NotificationDTO> getUnreadNotifications(UUID receiverId) {
        return notificationRepository.findNotificationsReadFalseByReceiverId(receiverId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void sendNotification(UUID receiverId) {
        List<NotificationDTO> notificationsDTOs = this.getUnreadNotifications(receiverId);
        try {
            notificationHandler.sendMessage(receiverId, notificationsDTOs);
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}
