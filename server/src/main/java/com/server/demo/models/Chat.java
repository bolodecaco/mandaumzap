package com.server.demo.models;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false, unique=true)
    private String whatsAppId;

    @Column(nullable = false)
    private String chatName;

    @Column(nullable = false)
    private boolean isGroup;

}
