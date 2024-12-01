package com.server.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.server.demo.enums.PlanType;
import com.server.demo.models.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {

    @Query("SELECT p FROM Plan p WHERE p.type = ?1")
    Optional<Plan> findByType(PlanType type);
}
