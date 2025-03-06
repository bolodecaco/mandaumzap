package com.server.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE m.deletedAt IS NULL AND m.userId = :userId")
    List<Message> findByDeletedAtIsNullAndUserId(String userId);

    Optional<Message> findByIdAndUserId(UUID id, String userId);

    List<Message> findBySessionIdAndUserId(UUID sessionId, String userId);
    List<Message> findByUserId(String userId);
}
