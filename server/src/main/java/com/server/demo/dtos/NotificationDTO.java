package com.server.demo.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class NotificationDTO {

    private UUID id;
    private String content;
    private boolean read;
    private String type;
    private UUID receiverId;

    public NotificationDTO(UUID id, String content, boolean read, String type, UUID receiverId) {
        this.id = id;
        this.content = content;
        this.read = read;
        this.type = type;
        this.receiverId = receiverId;
    }
}
