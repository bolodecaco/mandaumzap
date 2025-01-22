package com.server.demo.services;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;
import com.server.demo.repositories.ChatRepository;

class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create chat")
    void createChat() {
        RequestChatDTO requestChatDTO = new RequestChatDTO();
        requestChatDTO.setChatName("Meu chat");
        requestChatDTO.setWhatsAppId("123456789");
        Chat chat = new Chat();
        when(chatMapper.toEntity(requestChatDTO)).thenReturn(chat);
        chatService.createChat(requestChatDTO);
        assertNotNull(chat, "Chat não pode ser nulo");
    }

    @Test
    @DisplayName("Get chat by id")
    void getChatById() {
        UUID id = UUID.randomUUID();
        when(chatRepository.findById(id)).thenReturn(java.util.Optional.of(new Chat()));
        Chat chat = chatService.getChatById(id);
        assertNotNull(chat, "Chat não pode ser nulo");
    }

    @Test
    @DisplayName("Update chat")
    void updateChat() {
        UUID id = UUID.randomUUID();
        Chat chat = new Chat();
        chat.setId(id);
        when(chatRepository.findById(id)).thenReturn(java.util.Optional.of(chat));
        when(chatRepository.save(chat)).thenReturn(chat);
        UpdateChatDTO updateChatDTO = new UpdateChatDTO();
        updateChatDTO.setChatName("Updated Chat Name");
        chatService.updateChat(id, updateChatDTO);
        verify(chatRepository, times(1)).save(chat);
    }

    @Test
    @DisplayName("Delete chats")
    void deleteChat() {
        UUID id = UUID.randomUUID();
        Chat chat = new Chat();
        when(chatRepository.findById(id)).thenReturn(java.util.Optional.of(chat));
        chatService.deleteChat(id);
        verify(chatRepository, times(1)).deleteById(id);
    }

}
