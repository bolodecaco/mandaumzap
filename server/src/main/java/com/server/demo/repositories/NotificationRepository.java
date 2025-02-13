package com.server.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByReceiverId(String id);
    Optional<Notification> findByIdAndReceiverId(UUID id, String receiverId);

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId AND n.read = false")
    List<Notification> findUnreadNotificationsByReceiverId(@Param("receiverId") String receiverId);

}
