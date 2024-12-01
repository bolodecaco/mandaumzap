package com.server.demo.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.models.Chat;
import com.server.demo.models.User;
import com.server.demo.repositories.UserRepository;

@Service
public class ChatMapper {

    @Autowired
    private UserRepository userRepository;

    public ChatDTO toDTO(Chat chat) {
        return new ChatDTO(
                chat.getId(),
                chat.getChatId(),
                chat.getChatName(),
                chat.getOwner().getId()
        );
    }

    public Chat toEntity(RequestChatDTO chat) {
        Chat currentChat = new Chat();
        currentChat.setChatId(chat.getChatId());
        currentChat.setChatName(chat.getChatName());
        User owner = userRepository.findById(chat.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + chat.getOwnerId()));
        currentChat.setOwner(owner);
        return currentChat;
    }
}
