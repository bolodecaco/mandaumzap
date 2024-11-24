package com.server.demo.repositories;

import com.server.demo.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM Message m WHERE m.deletedAt IS NULL")
    List<Message> findByDeletedAtIsNull();

    @Query("SELECT m FROM Message m WHERE m.owner.id = :ownerId")
    List<Message> findByOwnerId(UUID ownerId);
}

