package com.server.demo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.server.demo.models.BroadcastList;

@Repository
public interface BroadcastListRepository extends JpaRepository<BroadcastList, UUID> {

    @Query("SELECT l FROM BroadcastList l WHERE l.owner.id = :ownerId")

    List<BroadcastList> findAllByUserId(UUID ownerId);
}
