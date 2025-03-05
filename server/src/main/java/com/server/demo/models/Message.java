package com.server.demo.models;

import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

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

    @Column(nullable = false)
    private String userId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "broadcast_list_id", nullable = true)
    private BroadcastList broadcastList;

    @Column(name = "times_sent", nullable = false)
    private int timesSent = 0;

    @Column(nullable = true, length = 3000)
    private String url;

    @Temporal(TemporalType.TIMESTAMP)
    private Date firstSentAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSentAt;

    @Column(nullable = true)
    private Date deletedAt;
}
