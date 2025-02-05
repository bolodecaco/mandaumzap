package com.server.demo.events;


import org.springframework.context.ApplicationEvent;

public class ConnectionEstablishedEvent extends ApplicationEvent {

    private final String receiverId;

    public ConnectionEstablishedEvent(Object source, String receiverId) {
        super(source);
        this.receiverId = receiverId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
