package com.server.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
    
    @ManyToOne
    @JoinColumn(name = "broadcast_list_id", nullable = true)
    private BroadcastList broadcastList;  

    @Column(name = "times_sent", nullable = false)
    private int timesSent = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date firstSentAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSentAt;

    @Column(nullable = true)
    private Date deletedAt; 
}
