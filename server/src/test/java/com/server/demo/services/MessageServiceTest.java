package com.server.demo.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.MessageSentToBotDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.exception.BusinessException;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.models.BroadcastList;
import com.server.demo.models.Message;
import com.server.demo.models.Session;
import com.server.demo.producer.MessageProducer;
import com.server.demo.repositories.MessageRepository;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private MessageProducer messageProducer;

    @Mock
    private BroadcastListService broadcastListService;

    @InjectMocks
    private MessageService messageService;

    private Message message;
    private MessageDTO messageDTO;
    private RequestMessageDTO requestMessageDTO;
    private String userId;

    @BeforeEach
    void setUp() {
        userId = "testUserId";
        
        message = new Message();
        message.setId(UUID.randomUUID());
        message.setContent("Test Message");
        message.setTimesSent(0);
        message.setUserId(userId);
        message.setSession(new Session());
        message.setBroadcastList(new BroadcastList());

        messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setTimesSent(message.getTimesSent());

        requestMessageDTO = new RequestMessageDTO();
        requestMessageDTO.setContent("New Message");
        requestMessageDTO.setSessionId(UUID.randomUUID());
        requestMessageDTO.setBroadcastListId(UUID.randomUUID());
    }

    @Test
    void shouldSendMessage() {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setWhatsAppId("123@c.us");
        
        when(messageRepository.findById(any())).thenReturn(Optional.of(message));
        when(messageRepository.save(any())).thenReturn(message);
        when(messageMapper.toDTO(any())).thenReturn(messageDTO);
        when(broadcastListService.getChatsFromList(any(), anyString())).thenReturn(Arrays.asList(chatDTO));

        MessageDTO result = messageService.sendMessage(message.getId());

        assertNotNull(result);
        assertEquals(messageDTO.getId(), result.getId());
        verify(messageProducer, times(1)).sendObject(any(MessageSentToBotDTO.class));
        verify(broadcastListService, times(1)).incrementMessageSent(any(), anyString());
    }

    @Test
    void shouldGetMessageById() {
        when(messageRepository.findByIdAndUserId(any(), anyString())).thenReturn(Optional.of(message));
        when(messageMapper.toDTO(message)).thenReturn(messageDTO);

        MessageDTO result = messageService.getMessageById(message.getId(), userId);

        assertNotNull(result);
        assertEquals(messageDTO.getId(), result.getId());
    }

    @Test
    void shouldGetMessagesBySessionId() {
        when(messageRepository.findBySessionIdAndUserId(any(), anyString())).thenReturn(Arrays.asList(message));
        when(messageMapper.toDTOList(any())).thenReturn(Arrays.asList(messageDTO));

        List<MessageDTO> result = messageService.getMessagesBySessionId(UUID.randomUUID(), userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(messageDTO.getId(), result.get(0).getId());
    }

    @Test
    void shouldSaveMessage() {
        when(messageMapper.toEntity(any())).thenReturn(message);
        when(messageRepository.save(any())).thenReturn(message);
        when(messageMapper.toDTO(message)).thenReturn(messageDTO);

        MessageDTO result = messageService.saveMessage(requestMessageDTO, userId);

        assertNotNull(result);
        assertEquals(messageDTO.getId(), result.getId());
    }

    @Test
    void shouldGetActiveMessages() {
        when(messageRepository.findByDeletedAtIsNullAndUserId(anyString())).thenReturn(Arrays.asList(message));
        when(messageMapper.toDTOList(any())).thenReturn(Arrays.asList(messageDTO));

        List<MessageDTO> result = messageService.getActiveMessages(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(messageDTO.getId(), result.get(0).getId());
    }

    @Test
    void shouldThrowExceptionWhenMessageNotFound() {
        when(messageRepository.findByIdAndUserId(any(), anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            messageService.getMessageById(UUID.randomUUID(), userId);
        });
    }

    @Test
    void shouldDeleteMessage() {
        when(messageRepository.findByIdAndUserId(any(), anyString())).thenReturn(Optional.of(message));
        when(messageRepository.save(any())).thenReturn(message);

        messageService.deleteMessage(message.getId(), userId);

        verify(messageRepository, times(1)).save(any());
    }
}
