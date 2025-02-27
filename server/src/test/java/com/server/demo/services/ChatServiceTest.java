package com.server.demo.services;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;
import com.server.demo.models.Session;
import com.server.demo.repositories.ChatRepository;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatService chatService;

    private Chat chat;
    private ChatDTO chatDTO;
    private RequestChatDTO requestChatDTO;
    private UpdateChatDTO updateChatDTO;
    private String userId;
    private String chatName;
    private String whatsAppId;
    private Session session;

    @BeforeEach
    void setUp() {
        userId = "testUserId";
        chatName = "testChatName";
        whatsAppId = "testWhatsAppId";
        session = new Session();
        session.setId(UUID.randomUUID());

        chat = new Chat();
        chat.setId(UUID.randomUUID());
        chat.setUserId(userId);
        chat.setChatName(chatName);
        chat.setWhatsAppId(whatsAppId);
        chat.setSession(session);

        chatDTO = new ChatDTO();
        chatDTO.setId(chat.getId());
        chatDTO.setChatName(chatName);
        chatDTO.setWhatsAppId(whatsAppId);
        chatDTO.setSessionId(session.getId());

        requestChatDTO = new RequestChatDTO();
        requestChatDTO.setChatName(chatName);
        requestChatDTO.setWhatsAppId(whatsAppId);
        requestChatDTO.setSessionId(session.getId());

        updateChatDTO = new UpdateChatDTO();
        updateChatDTO.setChatName(chatName);
        updateChatDTO.setWhatsAppId(whatsAppId);
    }

    @Test
    void shouldCreateChat() {
        when(chatMapper.toEntity(requestChatDTO)).thenReturn(chat);
        when(chatRepository.save(chat)).thenReturn(chat);
        when(chatMapper.toDTO(chat)).thenReturn(chatDTO);
        ChatDTO createdChatDTO = chatService.createChat(requestChatDTO, userId);
        assertNotNull(createdChatDTO);
        assertEquals(chatName, createdChatDTO.getChatName());
        assertEquals(whatsAppId, createdChatDTO.getWhatsAppId());
        assertEquals(session.getId(), createdChatDTO.getSessionId());
    }

    @Test
    void shouldGetChatDTOById() {
        UUID chatId = chat.getId();
        when(chatRepository.findByIdAndUserId(chatId, userId)).thenReturn(java.util.Optional.of(chat));
        when(chatMapper.toDTO(chat)).thenReturn(chatDTO);
        ChatDTO foundChatDTO = chatService.getChatDTOById(chatId, userId);
        assertNotNull(foundChatDTO);
        assertEquals(chatName, foundChatDTO.getChatName());
        assertEquals(whatsAppId, foundChatDTO.getWhatsAppId());
        assertEquals(session.getId(), foundChatDTO.getSessionId());
    }

    @Test
    void shouldGetChatById() {
        UUID chatId = chat.getId();
        when(chatRepository.findByIdAndUserId(chatId, userId)).thenReturn(java.util.Optional.of(chat));
        Chat foundChat = chatService.getChatById(chatId, userId);
        assertNotNull(foundChat);
        assertEquals(chatName, foundChat.getChatName());
        assertEquals(whatsAppId, foundChat.getWhatsAppId());
        assertEquals(session.getId(), foundChat.getSession().getId());
    }
}
