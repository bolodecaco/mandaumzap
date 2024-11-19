package com.server.demo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByReceiverId(UUID id);
    List<Notification> findNotificationsReadFalseByReceiverId(UUID receiverId);

}
