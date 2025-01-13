package com.server.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Session;
import com.server.demo.models.User;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findByUser(User user);
    List<Session> findByIsActive(boolean isActive);
    Optional<Session> findByUserAndIsActive(User user, boolean isActive);
    boolean existsByUserAndIsActive(User user, boolean isActive);
    void deleteByUser(User user);
}
