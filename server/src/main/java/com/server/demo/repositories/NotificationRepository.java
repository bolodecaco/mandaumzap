package com.server.demo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByReceiverId(UUID id);

    @Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId AND n.read = false")
    List<Notification> findNotificationsReadFalseByReceiverId(@Param("receiverId") UUID receiverId);

}
