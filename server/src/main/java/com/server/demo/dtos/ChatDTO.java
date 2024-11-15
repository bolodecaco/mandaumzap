package com.server.demo.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class ChatDTO {

    private UUID id;
    private String chatId;
    private String chatName;
    private UUID ownerId;

    public ChatDTO(UUID id, String chatId, String chatName, UUID ownerId) {
        this.id = id;
        this.chatId = chatId;
        this.chatName = chatName;
        this.ownerId = ownerId;
    }
}
