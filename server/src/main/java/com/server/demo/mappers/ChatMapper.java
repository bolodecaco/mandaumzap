package com.server.demo.mappers;

import org.springframework.stereotype.Service;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.models.Chat;

@Service
public class ChatMapper {

    public ChatDTO toDTO(Chat chat) {
        return new ChatDTO(
                chat.getId(),
                chat.getChatId(),
                chat.getChatName(),
                chat.getOwner().getId()
        );
    }
}
