package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.RequestNotificationDTO;
import com.server.demo.models.Notification;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    @Mapping(target = "receiverId", source = "notification.receiver.id")
    NotificationDTO toDTO(Notification notification);

    @Mapping(target = "receiver.id", source = "receiverId")
    Notification toEntity(RequestNotificationDTO notificationDTO);

    List<NotificationDTO> toDTOList(List<Notification> notifications);
}
