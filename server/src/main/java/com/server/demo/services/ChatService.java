package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.exception.BusinessException;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;
import com.server.demo.repositories.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMapper chatMapper;

    public List<ChatDTO> getAllChats(String userId) {
        List<Chat> chats = chatRepository.findAllByUserId(userId);
        return chatMapper.toDTOList(chats);
    }

    public ChatDTO getChatDTOById(UUID id, String userId) {
        Chat chat = chatRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException(String.format("Chat com id %s não encontrado", id)));
        return chatMapper.toDTO(chat);
    }

    public Chat getChatById(UUID id, String userId) {
        return chatRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException(String.format("Chat com id %s não encontrado", id)));
    }

    public ChatDTO createChat(RequestChatDTO chat, String userId) {
        Chat currentChat = chatMapper.toEntity(chat);
        currentChat.setUserId(userId);
        chatRepository.save(currentChat);
        return chatMapper.toDTO(currentChat);
    }

    public ChatDTO updateChat(UUID id, UpdateChatDTO chatDetails, String userId) {
        Chat existingChat = chatRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Chat com id %s não encontrado", id)));
        chatMapper.updateEntityFromDTO(chatDetails, existingChat);
        chatRepository.save(existingChat);
        return chatMapper.toDTO(existingChat);
    }

    public void deleteChat(UUID id, String userId) {
        Chat chatToBeDeleted = chatRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new BusinessException(String.format("Chat com id %s não encontrado", id)));
        chatRepository.delete(chatToBeDeleted);
    }
}

