package com.server.demo.events;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

public class ConnectionEstablishedEvent extends ApplicationEvent {

    private final UUID receiverId;

    public ConnectionEstablishedEvent(Object source, UUID receiverId) {
        super(source);
        this.receiverId = receiverId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }
}
