package com.server.demo.events;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

public class NotificationEvent extends ApplicationEvent {

    private final UUID receiverId;

    public NotificationEvent(Object source, UUID receiverId) {
        super(source);
        this.receiverId = receiverId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }
}
