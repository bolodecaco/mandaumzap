package com.server.demo.services;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.models.Message;
import com.server.demo.repositories.MessageRepository;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Save message")
    void saveMessage() {
        RequestMessageDTO requestMessageDTO = new RequestMessageDTO();
        Message message = new Message();
        when(messageMapper.toEntity(requestMessageDTO)).thenReturn(message);
        messageService.saveMessage(requestMessageDTO);
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    @DisplayName("Get message by id")
    void getMessageById() {
        UUID id = UUID.randomUUID();
        when(messageRepository.findById(id)).thenReturn(java.util.Optional.of(new Message()));
        messageService.getMessageById(id);
        verify(messageRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Send message")
    void sendMessage() {
        UUID messageId = UUID.randomUUID();
        Message message = new Message();
        when(messageRepository.findById(messageId)).thenReturn(java.util.Optional.of(message));
        messageService.sendMessage(messageId, new RequestMessageDTO());
        assertTrue(message.getTimesSent() >= 1, "O número de vezes que a mensagem foi enviada deve ser maior ou igual a 1");
    }

    @Test
    @DisplayName("Get message by user id")
    void getMessagesByUserId() {
        UUID userId = UUID.randomUUID();
        Message message = new Message();
        when(messageRepository.findByOwnerId(userId)).thenReturn(java.util.Arrays.asList(message));
        messageService.getMessagesByUserId(userId);
        verify(messageRepository, times(1)).findByOwnerId(userId);
    }

    @Test
    @DisplayName("Get active messages")
    void getActiveMessages() {
        Message message = new Message();
        when(messageRepository.findByDeletedAtIsNull()).thenReturn(java.util.Arrays.asList(message));
        messageService.getActiveMessages();
        verify(messageRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    @DisplayName("Delete message")
    void deleteMessage() {
        UUID messageId = UUID.randomUUID();
        Message message = new Message();
        when(messageRepository.findById(messageId)).thenReturn(java.util.Optional.of(message));
        messageService.deleteMessage(messageId);
        assertTrue(message.getDeletedAt() != null, "A mensagem deve ser deletada");
    }
}
