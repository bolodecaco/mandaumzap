package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.models.Chat;
import com.server.demo.repositories.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public List<ChatDTO> getAllChats() {
        return chatRepository.findAll().stream()
                .map(
                        chat -> new ChatDTO(
                                chat.getId(),
                                chat.getChatId(),
                                chat.getChatName(),
                                chat.getOwner().getId()
                        )
                )
                .collect(Collectors.toList());
    }

    public ChatDTO getChatById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        return new ChatDTO(
                chat.getId(),
                chat.getChatId(),
                chat.getChatName(),
                chat.getOwner().getId()
        );
    }

    public ChatDTO createChat(Chat chat) {
        Chat currentChat = chatRepository.save(chat);
        return new ChatDTO(
                currentChat.getId(),
                currentChat.getChatId(),
                currentChat.getChatName(),
                currentChat.getOwner().getId()
        );
    }

    public ChatDTO updateChat(UUID id, Chat chatDetails) {
        Chat existingChat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        if (chatDetails.getChatId() != null) {
            existingChat.setChatId(chatDetails.getChatId());
        }
        if (chatDetails.getChatName() != null) {
            existingChat.setChatName(chatDetails.getChatName());
        }
        Chat updatedChat = chatRepository.save(existingChat);
        return new ChatDTO(
                updatedChat.getId(),
                updatedChat.getChatId(),
                updatedChat.getChatName(),
                updatedChat.getOwner().getId()
        );
    }

    public void deleteChat(UUID id) {
        chatRepository.deleteById(id);
    }
}
