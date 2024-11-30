package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;
import com.server.demo.repositories.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMapper chatMapper;

    public List<ChatDTO> getAllChats() {
        List<Chat> chats = chatRepository.findAll();
        return chats.stream()
                .map(chatMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ChatDTO getChatDTOById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        return chatMapper.toDTO(chat);
    }

    public Chat getChatById(UUID id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public ChatDTO createChat(RequestChatDTO chat) {
        Chat currentChat = chatMapper.toEntity(chat);
        return chatMapper.toDTO(chatRepository.save(currentChat));
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
        return chatMapper.toDTO(updatedChat);
    }

    public void deleteChat(UUID id) {
        chatRepository.deleteById(id);
    }
}
