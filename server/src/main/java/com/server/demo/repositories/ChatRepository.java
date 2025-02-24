package com.server.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID>, JpaSpecificationExecutor<Chat> {

    List<Chat> findAllByUserId(String userId);

    Optional<Chat> findByIdAndUserId(UUID id, String userId);

    Optional<Chat> findByWhatsAppId(String whatsAppId);
}
