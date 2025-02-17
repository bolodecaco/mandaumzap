package com.server.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    List<Session> findByUserId(String userId);

    List<Session> findByIsActive(boolean isActive);

    Optional<Session> findByIdAndUserId(UUID id, String userId);

    void deleteAllByUserId(String userId);
}
