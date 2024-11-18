package com.server.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;


@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // @ManyToOne
    // @JoinColumn(name = "list_id")
    // private List list;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    @Value("${timesSent:1}")
    private int timesSent;

    @Column(name = "first_sent_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date firstSentAt;

    @Column(name = "last_sent_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date lastSentAt;

}
