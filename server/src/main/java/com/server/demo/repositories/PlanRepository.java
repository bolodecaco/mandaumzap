package com.server.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.demo.models.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {}