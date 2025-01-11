package com.server.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "routine")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActiveAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date willActiveAt;

    @Column(name = "times_sent", nullable = false)
    private int timesSent = 0;

    @PrePersist
    protected void onCreate() {
        if (lastActiveAt == null) {
            lastActiveAt = new Date();
        }
    }
}
