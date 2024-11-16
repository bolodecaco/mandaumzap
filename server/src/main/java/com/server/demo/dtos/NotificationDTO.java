package com.server.demo.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class NotificationDTO {

    private UUID id;
    private String content;
    private boolean read;
    private String type;

    public NotificationDTO(UUID id, String content, boolean read, String type) {
        this.id = id;
        this.content = content;
        this.read = read;
        this.type = type;
    }
}
