package com.server.demo.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "broadcast_lists")
public class BroadcastList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "broadcast_list_chats",
            joinColumns = @JoinColumn(name = "list_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<Chat> chats;

    @Column(nullable = false)
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActiveAt;

    @Column(nullable = false)
    private Integer messagesSent = 0;

    public BroadcastList() {
        this.messagesSent = 0;
    }
}
