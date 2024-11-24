package com.server.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "broadcast_list_id", nullable = true)
    private BroadcastList broadcastList;  

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = true)
    private Chat chatRecipient; 

    @Column(name = "times_sent", nullable = false)
    private int timesSent = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date firstSentAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSentAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt; 

    @PrePersist
    protected void onCreate() {
        if (firstSentAt == null) {
            firstSentAt = new Date();
        }
        lastSentAt = firstSentAt;
    }

    public void softDelete() {
        if (deletedAt == null) {
            this.deletedAt = new Date(); 
        }
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deletedAt != null;
}

}
