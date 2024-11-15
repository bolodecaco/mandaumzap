package com.server.demo.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.server.demo.models.Chat;

import lombok.Data;

@Data
public class BroadcastListDTO {

    private UUID id;
    private UUID owner;
    private List<ChatDTO> chats;
    private String title;
    private Date lastActiveAt;
    private int messagesSent;

    public BroadcastListDTO(UUID id, UUID owner, List<Chat> chats, String title, Date lastActiveAt, int messagesSent) {
        this.id = id;
        this.owner = owner;
        this.chats = this.transformChats(chats);
        this.title = title;
        this.lastActiveAt = lastActiveAt;
        this.messagesSent = messagesSent;
    }

    private List<ChatDTO> transformChats(List<Chat> chats) {
        return chats.stream()
                .map(
                        chat -> new ChatDTO(
                                chat.getId(),
                                chat.getChatId(),
                                chat.getChatName(),
                                owner
                        )
                )
                .collect(Collectors.toList());
    }
}
