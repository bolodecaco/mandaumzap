package com.server.demo.mappers;

import org.springframework.stereotype.Component;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.models.Notification;

@Component
public class NotificationMapper {

    public NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getContent(),
                notification.isRead(),
                notification.getType(),
                notification.getReceiver().getId());
    }

}
