package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
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
        return chatMapper.toDTOList(chats);
    }

    public ChatDTO getChatDTOById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Chat com id %s não encontrado", id)));
        return chatMapper.toDTO(chat);
    }

    public Chat getChatById(UUID id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Chat com id %s não encontrado", id)));
    }

    public ChatDTO createChat(RequestChatDTO chat) {
        Chat currentChat = chatMapper.toEntity(chat);
        chatRepository.save(currentChat);
        return chatMapper.toDTO(currentChat);
    }

    public ChatDTO updateChat(UUID id, UpdateChatDTO chatDetails) {
        Chat existingChat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Chat com id %s não encontrado", id)));
        if (chatDetails.getWhatsAppId() != null) {
            existingChat.setWhatsAppId(chatDetails.getWhatsAppId());
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
