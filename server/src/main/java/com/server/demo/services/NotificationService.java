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
import com.server.demo.exception.BusinessException;
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
        String receiverId = event.getReceiverId();
        sendUnreadNotifications(receiverId);
    }

    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notificationMapper.toDTOList(notifications);
    }

    public List<NotificationDTO> findUnreadByReceiverId(String receiverId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndReadFalse(receiverId);
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationByReceiverId(String receiverId) {
        List<Notification> notifications = notificationRepository.findByReceiverId(receiverId);
        return notificationMapper.toDTOList(notifications);
    }

    public NotificationDTO createNotification(Notification notification) {
        Notification existsNotification = notificationRepository.findById(notification.getId())
                .map(existing -> {
                    existing.setContent(notification.getContent());
                    existing.setReceiverId(notification.getReceiverId());
                    existing.setType(notification.getType());
                    existing.setRead(notification.isRead());
                    existing.setId(notification.getId());
                    return notificationRepository.save(existing);
                })
                .orElseGet(() -> notificationRepository.save(notification));
        eventPublisher.publishEvent(new NotificationEvent(this, existsNotification.getReceiverId()));
        return notificationMapper.toDTO(existsNotification);
    }

    public NotificationDTO updateRead(UUID id, boolean read, String receiverId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(id, receiverId)
                .orElseThrow(() -> new BusinessException(String.format("Notificação com id %s não encontrada.", id)));
        notification.setRead(read);
        Notification updatedNotification = notificationRepository.save(notification);
        NotificationDTO dto = notificationMapper.toDTO(updatedNotification);
        eventPublisher.publishEvent(new NotificationEvent(this, updatedNotification.getReceiverId()));
        return dto;
    }

    public NotificationDTO deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("Notificação com id %s não encontrada.", id)));
        notificationRepository.delete(notification);
        eventPublisher.publishEvent(new NotificationEvent(this, notification.getReceiverId()));
        return notificationMapper.toDTO(notification);
    }

    public List<NotificationDTO> getUnreadNotifications(String receiverId) {
        List<Notification> notifications = notificationRepository.findUnreadNotificationsByReceiverId(receiverId);
        return notificationMapper.toDTOList(notifications);
    }

    public void markAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }

    private void sendUnreadNotifications(String receiverId) {
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
    System.out.println("Evento recebido para: " + event.getReceiverId());
    sendUnreadNotifications(event.getReceiverId());
}

}
