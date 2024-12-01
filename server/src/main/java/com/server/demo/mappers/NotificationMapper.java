package com.server.demo.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.RequestNotificationDTO;
import com.server.demo.models.Notification;
import com.server.demo.models.User;
import com.server.demo.services.UserService;

@Component
public class NotificationMapper {

    @Autowired
    private UserService userService;

    public NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getContent(),
                notification.isRead(),
                notification.getType(),
                notification.getReceiver().getId());
    }

    public Notification toEntity(RequestNotificationDTO notificationDTO) {
        Notification notification = new Notification();
        User receiver = userService.findById(notificationDTO.getOwnerId());
        notification.setContent(notificationDTO.getContent());
        notification.setType(notificationDTO.getType());
        notification.setReceiver(receiver);
        return notification;
    }

}
