package com.server.demo.events;


import org.springframework.context.ApplicationEvent;

public class NotificationEvent extends ApplicationEvent {

    private final String receiverId;

    public NotificationEvent(Object source, String receiverId) {
        super(source);
        this.receiverId = receiverId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
