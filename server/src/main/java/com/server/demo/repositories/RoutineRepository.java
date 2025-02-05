package com.server.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.demo.models.Routine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface RoutineRepository extends JpaRepository<Routine, UUID> {
    List<Routine> findAllByUserId(String userId);
    Optional<Routine> findByIdAndUserId(UUID id, String userId);
}
