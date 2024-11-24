package com.server.demo.dtos;

import java.util.Date;
import java.util.UUID;


import lombok.Data;

@Data
public class MessageDTO {

    private UUID id;
    private String content;
    private Date lastSent;
    private int timesSent;
    private UUID ownerId;
    private UUID broadcastListId;
    private UUID chatId;

    public MessageDTO(UUID id, String content, Date lastSent, int timesSent, UUID ownerId, UUID broadcastListId, UUID chatId) {
        this.id = id;
        this.content = content;
        this.lastSent = lastSent;
        this.timesSent = timesSent;
        this.ownerId = ownerId;
        this.broadcastListId = broadcastListId;
        this.chatId = chatId;
    }
}
