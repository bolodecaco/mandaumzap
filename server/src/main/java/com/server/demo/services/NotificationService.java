package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.NotificationDTO;
import com.server.demo.events.ConnectionEstablishedEvent;
import com.server.demo.events.NotificationEvent;
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

    @Autowired
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @EventListener
    public void onConnectionEstablished(ConnectionEstablishedEvent event) {
        UUID receiverId = event.getReceiverId();
        sendUnreadNotifications(receiverId);
    }

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
        notificationRepository.save(notification);
        eventPublisher.publishEvent(new NotificationEvent(this, notification.getReceiver().getId()));
    }

    public NotificationDTO updateRead(UUID id, boolean read) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(read);
        Notification updatedNotification = notificationRepository.save(notification);
        eventPublisher.publishEvent(new NotificationEvent(this, updatedNotification.getReceiver().getId()));
        return notificationMapper.toDTO(updatedNotification);
    }

    public NotificationDTO deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
        eventPublisher.publishEvent(new NotificationEvent(this, notification.getReceiver().getId()));
        return notificationMapper.toDTO(notification);
    }

    public List<NotificationDTO> getUnreadNotifications(UUID receiverId) {
        return notificationRepository.findUnreadNotificationsByReceiverId(receiverId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void sendUnreadNotifications(UUID receiverId) {
        List<NotificationDTO> unreadNotifications = getUnreadNotifications(receiverId);

        try {
            var session = notificationHandler.getSession(receiverId);
            if (session != null && session.isOpen()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String notificationsJson = objectMapper.writeValueAsString(unreadNotifications);
                session.sendMessage(new TextMessage(notificationsJson));
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificações: " + e.getMessage());
        }
    }

    @EventListener
    public void onNotificationChanged(NotificationEvent event) {
        sendUnreadNotifications(event.getReceiverId());
    }
}
