package com.server.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.demo.models.BroadcastList;

@Repository
public interface BroadcastListRepository extends JpaRepository<BroadcastList, UUID> {
    List<BroadcastList> findAllByUserId(String userId);
    Optional<BroadcastList> findByIdAndUserId(UUID id, String userId);
}
