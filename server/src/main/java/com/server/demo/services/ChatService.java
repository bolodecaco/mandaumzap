package com.server.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.BotChatsDTO;
import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.exception.BusinessException;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;
import com.server.demo.models.Session;
import com.server.demo.repositories.ChatRepository;
import com.server.demo.repositories.SessionRepository;
import com.server.demo.specification.ChatSpecification;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ChatMapper chatMapper;

    public Page<ChatDTO> getAllChats(String userId, Pageable pageable, String search, String sessionId) {
        try {
            Specification<Chat> specification = ChatSpecification.withFilters(userId, search, UUID.fromString(sessionId));
            Page<Chat> chatsPage = chatRepository.findAll(specification, pageable);
            return chatsPage.map(chatMapper::toDTO);
        } catch (Exception e) {
            logger.error("Erro ao buscar chats: {}", e.getMessage());
            throw new BusinessException("Erro ao buscar chats");
        }
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

    public void insertChats(List<BotChatsDTO.BotResponseChats> chats, UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("Sessão não encontrada"));

        try {
            String userId = session.getUserId();
            Map<String, BotChatsDTO.BotResponseChats> uniqueChats = new HashMap<>();
            for (BotChatsDTO.BotResponseChats chat : chats) {
                uniqueChats.putIfAbsent(chat.getId(), chat);
            }
            List<Chat> chatEntities = uniqueChats.values().stream()
                    .filter(botChat -> chatRepository.findByWhatsAppId(botChat.getId()).isEmpty())
                    .map(botChat -> {
                        Chat chat = new Chat();
                        chat.setChatName(botChat.getName());
                        chat.setWhatsAppId(botChat.getId());
                        chat.setSession(session);
                        chat.setUserId(userId);
                        return chat;
                    })
                    .toList();
            if (!chatEntities.isEmpty()) {
                chatRepository.saveAll(chatEntities);
            }

        } catch (BusinessException e) {
            logger.error("Erro ao inserir chats: {}", e.getMessage());
        }
    }

}
